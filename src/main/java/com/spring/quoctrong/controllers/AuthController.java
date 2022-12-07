package com.spring.quoctrong.controllers;

import java.lang.ProcessBuilder.Redirect;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.quoctrong.models.ChangePasswordDTO;
import com.spring.quoctrong.models.User;
import com.spring.quoctrong.services.RegisterService;
import com.spring.quoctrong.services.SecurityService;

import Util.Utility;

@Controller
public class AuthController {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	RegisterService registerService;
	@Autowired
	SecurityService securityService;
	
	//LOGIN
	@GetMapping({"/login", "/login/"})
	public String login(RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("title", "Đăng nhập");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}
	
	
	//REGISTER
	@GetMapping({"/register", "/register/"})
	public String register(RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("title", "Đăng ký");
		User user = new User();
		model.addAttribute("user", user);
		
		return "register";
		
	}
	
	@PostMapping({"/register", "/register/"})
	public String register(@ModelAttribute(value = "user") User user, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			registerService.save(user);
			String siteURL = Utility.getSiteURL(request);
			registerService.sendVerificationEmail(user, siteURL);
			return "register-noti";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:register";
		}
		
		
	}
	
	@GetMapping({"/verify", "/verify/"})
	public String verifyAccount(@RequestParam(value = "code") String code, RedirectAttributes redirectAttributes) throws Exception {
		try {
			boolean verified = registerService.verify(code);
			if (verified) {
				redirectAttributes.addFlashAttribute("success", "Xác nhận email thành công!");
				return "redirect:/login";
			} else {
				return "redirect:/register";
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/register";
		}
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping({"/change-password", "/change-password/"})
	public String changePassword(Model model) {
		model.addAttribute("title", "Đổi mật khẩu");
		ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
		model.addAttribute("changePassword", changePasswordDTO);
		return "user/change-password";
	}
	
	@PostMapping({"/change-password", "change-password/"})
	public String changePassword(@ModelAttribute(value = "changePassword") ChangePasswordDTO changePasswordDTO, RedirectAttributes redirectAttributes
			,HttpServletRequest request
			) {
		try {
			securityService.changePassword(changePasswordDTO);
			redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
			request.logout();
			return "redirect:/login";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/change-password";
		}
	}
	
}
