package com.carolina.springboot.backend.apirest.models.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.carolina.springboot.backend.apirest.models.entity.Cliente;
import com.carolina.springboot.backend.apirest.models.entity.Region;
/*con el componente CrudRepository podemos acceder a todos los métodos básicos de un CRUD, save, findAll...
 * Además podemos implementar nuestros propios métodos*/
/*Spring.io podemos encontrar muchisima documentacion para implementar nuevos metodos
 * y consultar con @Query, que hay que recordar que estan escritar en HQL
 * por ejemplo: @Query(select u from User u where u.emailAddresss = ?1")
 * 				User findByEmailAddress(String emailAddress);
 * */
public interface IClienteDao extends JpaRepository<Cliente, Long> {
	//consulta jpa para que retorne todas las regiones
	@Query("from Region")
	public List<Region> findAllRegiones();

}
