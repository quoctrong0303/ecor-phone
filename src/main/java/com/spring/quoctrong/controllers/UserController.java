package com.spring.quoctrong.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.quoctrong.models.CustomUserDetails;
import com.spring.quoctrong.models.User;
import com.spring.quoctrong.services.UserService;
@Controller
public class UserController {
	@Autowired
	UserService userService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping({"/profile", "/profile/"})
	public String profile(Model model) {
		model.addAttribute("title", "Thông tin cá nhân");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
		User user = userService.findByEmail(principal.getUser().getEmail());
		model.addAttribute("user", user);
		return "user/profile";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping({"/profile", "/profile/"})
	public String updateProfile(@ModelAttribute(value = "user") User user, @RequestPart(value = "file") MultipartFile avatar, RedirectAttributes redirectAttributes) {
		try {
			userService.save(user, avatar);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/profile";
		}
		
		redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
		return "redirect:/profile";
	}
	
	
	
	
}
