package com.spring.quoctrong.services;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.SecureToken;
import com.spring.quoctrong.repositories.SecureTokenRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class SecureTokenService {

	@Autowired
	SecureTokenRepository secureTokenRepository;
	
	
	public SecureToken createSecureToken() {
		SecureToken secureToken = new SecureToken();
		String token = RandomString.make(64);
		secureToken.setToken(token);
		secureToken.setExpireAt(LocalDateTime.now().plusMinutes(30));
		secureTokenRepository.save(secureToken);
		return secureToken;
	}
}
