package com.spring.quoctrong;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.spring.quoctrong.models.CartItem;
import com.spring.quoctrong.services.CartService;
import com.spring.quoctrong.services.SecurityService;

@Component
public class AppRunner implements CommandLineRunner{

	@Autowired
	private SecurityService securityService;
	@Override
	public void run(String... args) throws Exception {
		//securityService.generateUsersRoles();		
	}
	
	
}
