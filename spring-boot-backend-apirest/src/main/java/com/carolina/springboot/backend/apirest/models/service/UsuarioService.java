package com.carolina.springboot.backend.apirest.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carolina.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.carolina.springboot.backend.apirest.models.entity.Usuario;
/*implementamos la interfaz la provee springsecurity, para el proceso de autentificacion con jpa(UserDetailsService).
 * Como es una consulta, importante que el transactional sea de spring y no de javax,anotamos la transaccion como de solo lectura.
 * */
//interfaz para trabajar con jpa y login, el proceso de autentificacion
@Service
public class UsuarioService implements IUsuarioService,UserDetailsService{
	//inyectamos la interfaz para usar los metodos
	@Autowired
	private IUsuarioDao usuarioDao;
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//obtenemos el usuario por su username, el metodo retorna un userDetails
		Usuario usuario =usuarioDao.findByUsername(username);
		//GrantedAuthority, 
		/*tenemos que convertir los roles  a un tipo GrantedAuthority. Para ello utilizamos stream(api de java)
		 * utilizamos el metodo map para convertir los datos del flujo. Por cada role lo convertimos en un tipo
		 * SimpleGrantedAuthority. Sigue siendo un stream de Authority, asi que por ultimo,
		 *  con collect( Collectors.tolist()) lo convertimos a tipo List.
		 *  De forma automatica tomamos la lista de roles, y lo convertimos a un tipo List SimpleGrantedAuthority, usando el 
		 *  metodo map. 
		 *  Es importante conocer los errores */
		if(usuario ==null) {
			logger.error("Error en el login: No existe el usuario en el sistema."+username );
			throw new UsernameNotFoundException("Error en el login: No existe el usuario"+username+ " en el sistema");
		}
		List<GrantedAuthority> authorities =usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info("Role: "+ authority.getAuthority()))//si quiero mostrar cada uno de los nombres de los roles, usamos el metodo peek
				.collect(Collectors.toList());
		
		//creamos la instancia del User, incluyendo los authorities., los obtenemos a traves de los usuario con los roles
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
	@Transactional(readOnly=true)
	@Override
	public Usuario findByUsername(String username) {
		
		return usuarioDao.findByUsername(username);
	}

}
