package com.carolina.springboot.backend.apirest.Controllers;


import java.io.IOException;
import java.net.MalformedURLException;
/*Para poder subir archivos tiene que ser a partir de form data, que soporte multipath.
 * Lo ideal para ello, es implementar la subida aparte. Para ello vamos a crear un metodo 
 * que llamaremos upload*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.carolina.springboot.backend.apirest.models.entity.Cliente;
import com.carolina.springboot.backend.apirest.models.entity.Region;
import com.carolina.springboot.backend.apirest.models.service.IClienteService;
import com.carolina.springboot.backend.apirest.models.service.IUploadFileService;
/*CORS permite a los navegadores movernos enviar y recibir datos restringidos, como pueden ser flujo de datos, 
 * css, script, imagenes..., desde un dominio a otro diferente que ha hecho la peticion. 
 * Es un mecanismo de control de acceso  http para acceder a varios recursos backend  que accede a través 
 * de un navegador que esta en otro dominio. El navegador se encarga de toda la materia de seguridad. 
 * El primer paso es configurar CORS en el servidor api rest, lo que nos permite determinar si es seguro,
 * o no, permitir algunas peticiones de origen cruzado.Para implementar CORS solo tenemos que anotar
 * en nuestro controlador @CrossOrigin(origins= {"http://localhost:4200"}
 * origins es donde se indica el dominio, puede soportar un arreglo, puede soportar una lista de dominios permitidos
 * en el caso de angular es localhost:4200. Damos acceso al dominio para que pueda recibir y enviar datos
 * tambien podemos anotar los metodos permitidos(get, post etc, si no ponemos nada, permitimos todo)
 *  */
/*
 * Creamos nuestra api rest, que es una url que vamos a usar para conectar y
 * enviar datos a nuestra aplicación en angular. Tenemos que mapear, para ello
 * generamos una url
 */
@CrossOrigin(origins = { "http://localhost:4200","*" })

@RestController
@RequestMapping("/api")
public class ClienteRestController {
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IUploadFileService uploadService;

	private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);

	/* con getmapping le damos una url especifica al metodo index */
	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	/*
	 * desde angular tenemos que pasar el numero de pagina.Y necesitaremos un
	 * pathvariable con el numero de pagina
	 */
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		// instancia de pageRequest a traves de su metodo of
		return clienteService.findAll(PageRequest.of(page, 4));
	}
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/clientes/{id}")
	/* PathVariable mapea el id de la uri al id de tipo long que usamos */
	/*
	 * ResponseEntity,nos sirve para poder manejar errores en el servidor, por
	 * ejemplo en el caso de buscar un cliente que no existe. ? identifica que puede
	 * devolver cualquier tipo de objeto. Ya que va a ser distinto si encuentra al
	 * cliente que si no lo hace
	 */
	public ResponseEntity<?> mostrar(@PathVariable Long id) {
		/*
		 * Spring tiene un error que nos permite manejar los problemas que sucedan en la
		 * bbdd, como conexiones, por lo tanto es adecuado implementar el metodo en un
		 * try catch que nos lo facilite
		 */
		Cliente cliente = null;
		// para mostrar un mensaje con el objeto creamos un map de string, object
		Map<String, Object> response = new HashMap<>();
		try {
			cliente = clienteService.findById(id);

		} catch (DataAccessException e) {
			// añadimos el mensaje al map
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			// ahora retornamos el response, que es de tipo <Map<String, Object>
			// y en el constructor pasamos el response
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			// de este modo ya controlamos el error
		}

		if (cliente == null) {
			response.put("mensaje",
					"El cliente con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		// en el caso de encontrar al cliente devuelve el cliente en formato json y un
		// estado
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);

	}

	// post= sirve para crear nueva informacion
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/clientes")
	/*
	 * Cliente viene en formato json del request por eso usamos la anotacion
	 * RequestBody
	 */
	public ResponseEntity<?> crear(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteNuevo = null;
		Map<String, Object> response = new HashMap<>();
		/*
		 * En el if tenemos la forma de validar la información a traves de una lista de
		 * errores que los recoge y los muestra, pero esta es la manera antigua. A
		 * partir de Java 8 podemos hacerlo de una forma mas sencilla a traves de un
		 * Stream de String if(result.hasErrors()){ List<String> errors = new
		 * ArrayList<>(); for(FieldError err: result.getFieldErrors()){
		 * errors.add("El campo '"+err.getField()+"' "+err.getDefaultMessage()); }
		 * response.put("errors",errors); return new ResponseEntity<Map<String,
		 * Object>>(response, HttpStatus.BAD_REQUEST); }
		 * 
		 */
		// manejo de errores en la aplicacion con el jdk8
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			clienteNuevo = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido creado con éxito!");
		response.put("cliente", clienteNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// para update usamos put
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PutMapping("/clientes/{id}")
	/*
	 * Cliente viene en formato json del request por eso usamos la anotacion
	 * RequestBody
	 */
	public ResponseEntity<?> modificar(@Valid @RequestBody Cliente cliente, @PathVariable Long id,
			BindingResult result) {
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteActualizado = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (clienteActual == null) {
			response.put("mensaje", "Error: no se puede editar el cliente con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			//Como desde angular enviamos un cliente con la informacion de la region, es este valor el que guardamos
			clienteActual.setRegion(cliente.getRegion());
			clienteActualizado = clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el update en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ha sido actualizado con éxito!");
		response.put("cliente", clienteActualizado);
		// save nos sirve tanto para hacer un insert como para un update

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/clientes/{id}")
	/*
	 * Cliente viene en formato json del request por eso usamos la anotacion
	 * RequestBody
	 */
	public ResponseEntity<?> borrar(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		// Cuando borramos un cliente borramos su foto, para que esta no quede huerfana
		try {
			Cliente cliente = clienteService.findById(id);
			// tenemos que validar si el cliente existente tiene una foto anterior para
			// sustituirla
			String nombreFotoAnterior = cliente.getFoto();
			uploadService.eliminar(nombreFotoAnterior);
			clienteService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar un delete en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido eliminado con éxito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*
	 * Este metodo como todos va a devolver un responseEntity, RequestParam se
	 * obtiene directamente desde el objeto request, y lo inyecta. Le damos un
	 * nombre entre"" y despues lo usamos. Vamos a tener el objeto Map, porque
	 * tenemos que pasar mensajes al usuario, para manejar errores y enviar mensaje
	 * al usuario. Ahora tenemnos que subir el archivo, que conlleva buscar el
	 * archivo y ASIGNARSELO al cliente por lo que tenemos que tener un atributo mas
	 * en la entidad cliente. Es importante tener en cuenta que la ruta que elegimos
	 * en el equipo, esta en nuestro servidor para acceder a ella leerla y
	 * mostrarla. Por lo que es un directorio externo.
	 *
	 */
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		// Como tenemos que buscar el cliente al cual le añadiremos la foto tenemos que
		// capturarlo
		Cliente cliente = clienteService.findById(id);
		// Debemos validar si el archivo existe
		if (!archivo.isEmpty()) {
				String nombreArchivo=null;
			// Ahora con files.copy copia el archivo que hemos subido al servidor a la ruta
			// escogida
			try {
				nombreArchivo=uploadService.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente : ");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			// tenemos que validar si el cliente existente tiene una foto anterior para
			// sustituirla
			String nombreFotoAnterior = cliente.getFoto();
			uploadService.eliminar(nombreFotoAnterior);
			
			cliente.setFoto(nombreArchivo);
			clienteService.save(cliente);
			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// metodo handler que nos muestre la foto en el navegador, este metodo recibe un
	// pathvariable la ruta de la foto
	// la convertimos en un recurso y este recurso lo guardamos en el responseEntity
	// y lo mostramos en el navegador, a traves de la respuesta
	// :.+ expresion regular que indica que va a tener una extension el {nombreFoto}
	// es el pathVariable
	@GetMapping("uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {
		Resource recurso=null;
		try {
			recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		// y por ultimo tenemos que pasar las cabeceras para forzar que esta imagen se
		// pueda descargar
		//en las cabeceras definimos el tipo de recursos que vamos a enviar, por ejemplo multipath
		HttpHeaders cabecera = new HttpHeaders();
		// ademas como valor añadimos attachment, forzando en la respuesta que se
		// descargue en el navegador, pudiendo mostrar la imagen
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");// usamos
																									// comillas
		// por ultimo tenemos que pasar la cabecera en el response
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);

	}
	//@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/clientes/regiones")
	public List<Region> listarRegiones() {
		return clienteService.findAllRegiones();
	}
	

}
