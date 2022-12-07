package com.spring.quoctrong.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.quoctrong.models.ChangePasswordDTO;
import com.spring.quoctrong.models.CustomUserDetails;
import com.spring.quoctrong.models.Role;
import com.spring.quoctrong.models.User;
import com.spring.quoctrong.repositories.RoleRepository;
import com.spring.quoctrong.repositories.UserRepository;

@Service
public class SecurityService implements UserDetailsService{
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public User createUser(String name, String password, Role ...roles) {
		User user = new User(name, passwordEncoder.encode(password));
		for (Role role : roles) {
			user.addRole(role);
		}
		return user;
	}
	@Transactional
	public void generateUsersRoles() {
		Role roleAdmin = new Role("ADMIN");
	    Role roleUser = new Role("USER");
	    Role roleAuthor = new Role("AUTHOR");
	    Role roleEditor = new Role("EDITOR");
	    roleRepository.save(roleAdmin);
	    roleRepository.save(roleUser);
	    roleRepository.save(roleAuthor);
	    roleRepository.save(roleEditor);
	    roleRepository.flush();
	    
	    User admin = createUser("admin", "123", roleAdmin);
	    userRepository.save(admin);
	    
	    User bob =  createUser("bob", "123", roleUser);
	    userRepository.save(bob);
	    
	    userRepository.flush();
	}
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);	
		 if (user == null) {
	            throw new UsernameNotFoundException(email);	 
}
		 return new CustomUserDetails(user);
	}
	
	public void changePassword(ChangePasswordDTO changePasswordDTO) throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
			throw new Exception("Mật khẩu không khớp!");
		}
		if (this.passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getUser().getPassword())) {
			user.getUser().setPassword(this.passwordEncoder.encode(changePasswordDTO.getNewPassword()));
			userRepository.save(user.getUser());
		} else {
			throw new Exception("Mật khẩu cũ không đúng!");
		}
	}
}
