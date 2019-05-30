package com.carolina.springboot.backend.apirest.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.carolina.springboot.backend.apirest.models.dao.IClienteDao;
import com.carolina.springboot.backend.apirest.models.dao.IFacturaDao;
import com.carolina.springboot.backend.apirest.models.dao.IProductoDao;
import com.carolina.springboot.backend.apirest.models.entity.Cliente;
import com.carolina.springboot.backend.apirest.models.entity.Factura;
import com.carolina.springboot.backend.apirest.models.entity.Producto;
import com.carolina.springboot.backend.apirest.models.entity.Region;

/*Puede contener a las clases daos y dentro sus metodos podrian interactuar diferentes DAO * 
 * y todo bajo una sola transaccion pudiando trabajar juntos diferentes dao, con el service
 * intentamos. Evitamos ensuciar el controlador con las clases daos de forma directa
 * desacoplando el codigo y llevandolo a una fachada. */
/**/
@Service /*
			 * Decoramos la clase como un componente de Servcio en el contenedor de Spring
			 */
public class ClienteServiceImpl implements IClienteService {
	/*
	 * Usando autowired inyectamos el cliente Dao, pudiendo asi usar en el metodo
	 * find all de clienteDao, que nos devolverá la lista de clientes.
	 */
	@Autowired
	private IClienteDao clienteDao;
	@Autowired
	private IFacturaDao facturaDao;
	@Autowired
	private IProductoDao productoDao;
	/*
	 * nos permite manejar transacciones, como es un select es solo lectura, por eso
	 * es readOnly
	 */
	/*
	 * Al anotarla en el service estamos sobreescribiendo la transaccionalidad que
	 * viene en el crudRepository de Spring. Sin embargo todos los metodos nuevos
	 * que creemos en ClienteDao deben tener la anotacion de @Transaccional
	 */

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		
		return clienteDao.findAll(pageable);
	}
	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		// al ser tipo optional nos permite especificar el metodo que vamos a usar,
		// con el método get, nos devuelve el objeto si lo encuentra, sino nos da una
		// exception
		// si usamos orElse, si no lo encuentra nos devuelve nulo
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional()
	public Cliente save(Cliente cliente) {

		return clienteDao.save(cliente);
	}

	@Override
	public void delete(Long id) {
		clienteDao.deleteById(id);

	}
	@Transactional(readOnly = true)
	@Override
	public List<Region> findAllRegiones() {
		
		return clienteDao.findAllRegiones();
	}
	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {		
		return facturaDao.findById(id).orElse(null);
	}
	@Override
	@Transactional
	public Factura saveFactura(Factura factura) {
		return facturaDao.save(factura);
	}
	@Override
	@Transactional(readOnly = true)
	public void deleteFacturaById(Long id) {
		facturaDao.deleteById(id);
	}
	
	@Override
	public List<Producto> findProductoByNombre(String nombre) {
		return productoDao.findByNombreContainingIgnoreCase(nombre);
		 
	}

	

}
