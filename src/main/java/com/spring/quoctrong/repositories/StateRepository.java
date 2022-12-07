package com.spring.quoctrong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer>{

}
