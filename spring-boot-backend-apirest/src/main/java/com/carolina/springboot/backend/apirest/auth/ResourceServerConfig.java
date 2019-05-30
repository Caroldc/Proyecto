package com.carolina.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/*https://docs.spring.io/spring-security/site/docs/5.1.2.RELEASE/reference/htmlsingle/#cors*/
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/clientes","/api/clientes/regiones","/api/clientes/page/**","/api/uploads/img/**","/images/**").permitAll()//tienen acceso todos los usuarios
		//.antMatchers("/api/clientes/{id}").permitAll()
	//	.antMatchers("/api/facturas/**").permitAll()
		/*.antMatchers(HttpMethod.GET,"/api/clientes/{id}").hasAnyRole("USER","ADMIN")
		.antMatchers(HttpMethod.POST,"/api/clientes/upload").hasAnyRole("USER","ADMIN")
		.antMatchers(HttpMethod.POST,"/api/clientes/").hasRole("ADMIN")
		.antMatchers("/api/clientes/**").hasRole("ADMIN")*///Como no indicamos el tipo de metodo se aplicara para todos los metodos
		.anyRequest().authenticated()
		.and().cors().configurationSource(corsConfigurationSource());//siempre hay que poner esto al final, porque asi todas las rutas no especificadas
		//utilizamos el metodo and para volver al objeto principal (HttpSecurity). cors.configurationSource y pasamos el metodo
		//que da la configuracion
		//van a ser para usuarios autentificados
	}
	/*
	 * Todo lo que es el intercambio de recursos tiene que ver con Cors, utiliza las cabeceras http(headers) para permitir a un cliente que esta en distinto lugar que el backend
	 * tenga acceso.
	 * Lo primero es crear un metodo anotado por Bean() es importante hacer las importaciones desde el cors normal, y NO del reactive
	 * */
	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
		 //creamos la instancia del CorsConfiguration
	        CorsConfiguration config = new CorsConfiguration();
	        //Apartir de config vamos a configurar nuestro Cors
	        //Lo primero vamos a permitir nuestros dominios, nuestras aplicaciones clientes, en nuestro caso el cliente Angular
	        config.setAllowedOrigins(Arrays.asList("http://localhost:4200","*"));//agregamos nuestra ruta o nombre de dominio, colocando * acepta cualquier origen
	        //Configurar todos los metodos que vamos a permitir
	        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE", "OPTIONS"));
	        //Vamos a configurar el permitir credenciales en true
	        config.setAllowCredentials(true);
	        //y por ultimo las cabeceras, indicamos nuestros tipos de cabeceras
	        config.setAllowedHeaders(Arrays.asList("Content-Type","Authorization"));
	        //Ahora registramos esta configuracion de Cors para todas nuestras rutas del backend
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);
	        return source;
	        /**/
	    }
	 /*Por ultimo tenemos que registrar un filtro en la prioridad mas alta, para que se aplique tanto al servidor de configuracion
	  * como a los endpoint para autenticarnos y generar el token Y oara cada vez que queramos validar nuestro token*/
	 @Bean
	 public FilterRegistrationBean<CorsFilter> corsFilter(){
		 //Hay que indicar el tipo de dato que vamos a registrar, en nuestro caso CorsFilter
		 //Creamos la instancia de la clase, y por argumento le pasamos la instancia de corsFilter y al corsFilter la pasamos la configuracion
		 //que creamos anteriormente
		 FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		 /*Cuanto mas bajo el orden mayor es la prioridad, elegimos la más alta*/
		 bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		 return bean;
		 
	 }
	 /*Basicamente creamos un filtro al que pasamos toda la configuracion y lo registramos  con una prioridad alta. De esta forma queda configurado
	  * los endpoint para autentificarnos con el token y para validar el token*/

}
