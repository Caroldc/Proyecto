package com.carolina.springboot.backend.apirest.Controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carolina.springboot.backend.apirest.models.entity.Factura;
import com.carolina.springboot.backend.apirest.models.entity.Producto;
import com.carolina.springboot.backend.apirest.models.service.IClienteService;

@CrossOrigin(origins = { "http://localhost:4200","*" })
@RestController
@RequestMapping("/api")
public class FacturaRestController {
	@Autowired
	private IClienteService clienteService;
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/facturas/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Factura show(@PathVariable Long id) {
		return clienteService.findFacturaById(id);
	}
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/facturas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		clienteService.deleteFacturaById(id);
		
	}
	/*método para filtrar los productos por nombre*/
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@GetMapping("/facturas/filtrar-productos/{texto}")
	@ResponseStatus(HttpStatus.OK)
	public List<Producto> filtrarProductos(@PathVariable String texto){
		return clienteService.findProductoByNombre(texto);
	}
	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PostMapping("/facturas")
	@ResponseStatus(HttpStatus.CREATED)
	public Factura crear( @RequestBody Factura factura) {
			try {
			return clienteService.saveFactura(factura);
		} catch (DataAccessException e) {
			return null;
		}
			
		
	}
	
}
