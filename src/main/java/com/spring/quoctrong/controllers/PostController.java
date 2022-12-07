package com.spring.quoctrong.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.quoctrong.models.Post;
import com.spring.quoctrong.services.PostService;

@Controller
public class PostController {

	@Autowired
	PostService postService;
	
	@GetMapping({"/post/{id}", "/post/{id}/"})
	public String view(Model model, @PathVariable(value = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			Post post = postService.findById(id).orElseThrow(() -> new Exception("Bài viết không tồn tại!"));
			model.addAttribute("post", post);
			model.addAttribute("title", post.getTitle());
			return "user/post";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/";
		}
		
	}
	
}
