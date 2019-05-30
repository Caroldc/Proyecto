package com.carolina.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.carolina.springboot.backend.apirest.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{
	
	/*Método para que retorne una lista de productos buscados por el nombre usando JQL
	 * @Query("select p from Producto p where p.nombre like %?1%")
	 * public List<Producto> findByName(String nombre);*/
	
	
	public List<Producto> findByNombreContainingIgnoreCase(String nombre);
	/*Buscamos por un nombre que contenga(containing , palabra reservada de spring) ignorando mayusculas y minusculas(IgnoreCase)*/
	
	

}
