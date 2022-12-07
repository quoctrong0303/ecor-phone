package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.State;
import com.spring.quoctrong.repositories.StateRepository;

@Service
public class StateService {

	@Autowired
	StateRepository stateRepository;
	
	public List<State> findAll() {
		return stateRepository.findAll();
	}
	public Optional<State> findById(Integer id) {
		return stateRepository.findById(id);
	}
}
