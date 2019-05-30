package com.carolina.springboot.backend.apirest.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carolina.springboot.backend.apirest.models.entity.Cliente;
import com.carolina.springboot.backend.apirest.models.entity.Factura;
import com.carolina.springboot.backend.apirest.models.entity.Producto;
import com.carolina.springboot.backend.apirest.models.entity.Region;
/*Aqui añadimos los métodos del crud*/
public interface IClienteService {
	/*CRUD Completo*/
	public List<Cliente> findAll();
	
	//Este método nos permite paginar 
	public Page<Cliente> findAll(Pageable pageable);
	public Cliente findById(Long id);
	public Cliente save (Cliente cliente);
	public void delete(Long id);
	public List<Region> findAllRegiones();
	//un service puede tener varios atributos de varios daos
	public Factura findFacturaById(Long id);
	public Factura saveFactura (Factura factura);
	public void deleteFacturaById(Long id);
	public List<Producto> findProductoByNombre(String nombre);

}
