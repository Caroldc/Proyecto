package com.carolina.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="roles")
public class Role implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true, length=20)
	private String nombre;
	/*nombre del atributo que contiene la otra clase, que seria roles
	/el dueño de la relacion es usuarios, y roles, la inversa
	@ManyToMany(mappedBy="roles")
	private List<Usuario>usuarios;
	pero como no es necesario tener una lista de usuarios en roles, omitimos esta iocion
	*/
	public Long getId() {
		return id;
	}
       void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

/*
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

*/
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
