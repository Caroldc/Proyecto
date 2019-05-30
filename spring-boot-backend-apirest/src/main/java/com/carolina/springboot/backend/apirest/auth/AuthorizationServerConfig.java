package com.carolina.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
/*Todo lo que esta relacionado con JWT lo configuramos en estaclase.*/
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	/*authenticationManager, no es un bean, por lo que debemos arreglar esto para poder hacer una inyeccion
	 * de dependencias. Para ello creamos un metodo que devuelve un authenticationManager, y lo anotamos
	 * con BEAN*/
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {		
		security.tokenKeyAccess("permitAll()")//acceso para todos los usuarios, para poder identificarse-> aqui se genera el token
		.checkTokenAccess("isAuthenticated()");//permiso al endpoint que va a validar el token-> aqui se valida el token
		/*Estos token estan validados por authorization basica, se envia el cliente id con su
		 * secreto. Luego se envian en las cabeceras de la autentificacion del tipo basic.*/
		
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("angularapp")//cliente
		.secret(passwordEncoder.encode("12345"))//clave
		.scopes("read","write") //permiso que va a tener el cliente
		.authorizedGrantTypes("password","refresh_token")//usamos password cuando tenemos usuarios con credenciales 
		.accessTokenValiditySeconds(3600)//validez del token, tiempo de caducidad (1h)
		.refreshTokenValiditySeconds(3600);//validez del refresh token (1h)
		
	}
	
	
	/*Se encarga de todo el proceso de autenticacion y validar el token. Si todo sale bien genera el token y con el
	 * token nuestro usuario puede acceder a los distintos recursos*/
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {		
		//cuando añadimos informacion adicional hay que añadirla en este apartado
		//cadena qu e une la informacion del token, con la nueva informacion. La que viene por defecto y la que agregamos
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));		
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore())
		.accessTokenConverter(accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);		
	}
	@Bean
	public JwtTokenStore tokenStore() {		
		return new JwtTokenStore(accessTokenConverter());
	}
	/*Es muy importante crear el metodo en public*/
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVADA);
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLICA);
		return jwtAccessTokenConverter;
	}
	
	
	
}
