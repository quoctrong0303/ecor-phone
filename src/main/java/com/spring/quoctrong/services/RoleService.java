package com.spring.quoctrong.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.Role;
import com.spring.quoctrong.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	public List<Role> findAll() {
		return roleRepository.findAll();
	}
}
