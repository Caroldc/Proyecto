package com.carolina.springboot.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
/*Configuracion de springSecurity con respecto authentication manager, tenemos que registrar la clase servicio
 que creamos anteriormente(UsuarioService) que implementa la interfaz userDetailsService, la tenemos que registrar en el authentication manager
 para que se pueda realizar el proceso de autentificacion con jpa */
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*Registrar en el authentication manager esta funcionalidad, por lo que sobreescribimos el metodo  configure((AuthenticationManagerBuilder).
 Registramos el metodo userDetailsService y le pasamos nuestro atributo usuarioService.
 Lo siguiente es configurar nuestra pass, y pasamos una instancia de implementacion de encriptacion. Para ello
 creamos un nuevo metodo passwordEncoder, que devolvera un BCryptPasswordEncoder().
 Ademas tenemos que registrar este objeto que estamos creando como un componente de spring(BEAN), puesto que lo volveremos a usar
 en varias ocasiones, por lo que nos conviene poder inyectarlo. Para ello utilizamos la anotacion BEAN, que nos permite
 registrar los objetos que devuelve un método.*/
@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService usuarioService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
	//	super.configure(auth);
	}
	@Bean
	@Override	
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	/*Tambien podemos agregar reglas de httpsecurity pero por el lado de spring .
	 * Tenemos que deshabilitar algunas protecciones y las sesiones, van a ser sin estado stateless*/
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest().authenticated().and()
		.csrf().disable()//cross-site request forgery, falsificacion de peticion en sitios cruzados(para proteger el formulario.)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//sin estado en sesiones, porque utilizamos token
		//asi que no es necesario
	}

	

}
