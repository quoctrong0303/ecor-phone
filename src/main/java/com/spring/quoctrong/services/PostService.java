package com.spring.quoctrong.services;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.quoctrong.models.CustomUserDetails;
import com.spring.quoctrong.models.Post;
import com.spring.quoctrong.repositories.PostRepository;
import com.spring.quoctrong.services.FilesStorageService;

@Service
public class PostService {

	@Autowired
	PostRepository postRepository;
	@Autowired
	private FilesStorageService filesStorageService;
	
	public List<Post> findAll() {
		return postRepository.findAll();
	}
	
	public Optional<Post> findById(Integer id) {
		return postRepository.findById(id);
	}
	
	public void deleteById(Integer id) {
		postRepository.deleteById(id);
	}
	
	public Post update(Post post, MultipartFile file) throws Exception{
		try {
			if (file != null && !file.isEmpty()) {
				String thumbnail = filesStorageService.save(file);
				post.setThumbnail(thumbnail);
			} else {
				Post oldPost = postRepository.findById(post.getId()).orElseThrow(() -> new Exception("Bài viết không tồn tại!"));
				post.setThumbnail(oldPost.getThumbnail());
			}
			return postRepository.save(post);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Post save(Post post, MultipartFile file) {
		String thumbnail = filesStorageService.save(file);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		post.setUser(userDetails.getUser());
		post.setThumbnail(thumbnail);
		return postRepository.save(post);
	}
	
	public List<Post> findTop3ByOrderByCreatedDate() {
		return postRepository.findTop3ByOrderByCreatedDateDesc();
	}
}
