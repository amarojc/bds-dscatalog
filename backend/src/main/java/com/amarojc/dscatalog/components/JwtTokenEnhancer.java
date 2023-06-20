package com.amarojc.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.amarojc.dscatalog.entities.User;
import com.amarojc.dscatalog.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer{

	@Autowired
	private UserRepository userRepository;
	
	/*
	 * Ao implementar o método ele irá entrar no ciclo de vida do token, e ao gerar o token ele
	 * irá acrescentar os objetos que foram passados.
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		User user = userRepository.findByEmail(authentication.getName());
		Map<String, Object> map = new HashMap<>();
		map.put("userFistName", user.getFirstName());
		map.put("userId", user.getId());
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		token.setAdditionalInformation(map);
		
		return accessToken;
	}

}
