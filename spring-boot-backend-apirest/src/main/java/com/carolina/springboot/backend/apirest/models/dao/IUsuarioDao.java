package com.carolina.springboot.backend.apirest.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.carolina.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	//con crudrepository no se puede paginar, porque no trae los metodos
	/*A traves del nombre del método (Query method name) se ejecutará*
	 * la consutal de JPQL select u from Usuario u where u.username=?
	 * en la documentacion de spring data vienen ejemplos*/
	public Usuario findByUsername(String username);
	/*@Query("select u from Usuario u where u.username=?1")
	public Usuario findByUsername2(String username);*/
	
}
