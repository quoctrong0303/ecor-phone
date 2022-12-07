package com.spring.quoctrong.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
	List<Post> findTop3ByOrderByCreatedDateDesc();
}
