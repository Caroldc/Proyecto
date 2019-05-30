package com.carolina.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*Podemos validar los datos desde el lado del servidor, o desde el lado del cliente.
 * Con javaBean validator*/
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "no puede estar vacío")
	@Size(min = 3, max = 12, message = "El tamaño tiene que estar entre 3 y 12 caracteres")
	@Column(nullable = false)
	private String nombre;
	@NotEmpty(message = "no puede estar vacío")
	private String apellido;
	@Column(nullable = false, unique = true)
	@NotEmpty(message = "no puede estar vacío")
	@Email(message = "no es un correo válido")
	private String email;
	@NotNull(message = "no puede estar vacío")
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;/*
							 * //coon prePersist hacemos que se cree la fecha de forma automatica cuando se
							 * crea el objeto
							 * 
							 * @PrePersist public void prePersist() { createAt= new Date(); }
							 */
	private String foto;
	// Como hay una relacion de muchos clientes viven en una region
	// propios de hibernate
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@NotNull(message = "La region no puede ser nula")
	private Region region;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"clientes", "hibernateLazyInitializer", "handler"})
	private List<Factura> facturas;

	public Cliente() {
		this.facturas = new ArrayList<>();
	}

	/*
	 * @JsonIgnoreProperties({"hibernateLazyInitializer","handler"}), al haber
	 * anotado con lazy el atributo, no se cargan los datos hasta que accedamnos a
	 * el, por lo que si llamamos al objeto el json que nos devuelve no tiene el
	 * contenido de region, y saltaria un error. Por eso lo excluimos del proxy con
	 * esta propiedad
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
