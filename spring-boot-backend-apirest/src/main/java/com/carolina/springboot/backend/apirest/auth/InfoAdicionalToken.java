package com.carolina.springboot.backend.apirest.auth;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.carolina.springboot.backend.apirest.models.entity.Usuario;
import com.carolina.springboot.backend.apirest.models.service.IUsuarioService;

@Component
public class InfoAdicionalToken implements TokenEnhancer{
	@Autowired
	private IUsuarioService usuarioService;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Map<String, Object> info = new HashMap<>();//añadimos la informadion adicional que queramos incluir en el access Token
		info.put("informacion adicional", "Hola que tal! :".concat(authentication.getName()));//insertamos un saludo al usuario cuando se conecte
		info.put("nombre",usuario.getNombre());
		info.put("apellido",usuario.getApellido());
		info.put("email",usuario.getEmail());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);//clase concreta(DefaultOAuth2AccessToken) que implementa la interfaz de accessToken;
		//y por ultimo añadimos el map con los datos extras 
		return accessToken;//devolcemos el accessToken
	}

}
