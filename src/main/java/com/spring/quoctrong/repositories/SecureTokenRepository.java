package com.spring.quoctrong.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.SecureToken;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureToken, Integer>{

	Optional<SecureToken> findByToken(String token);
	void deleteByToken(String token);
}
