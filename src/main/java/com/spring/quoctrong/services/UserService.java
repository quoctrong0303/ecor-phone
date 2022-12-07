package com.spring.quoctrong.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.Multipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.quoctrong.models.CustomUserDetails;
import com.spring.quoctrong.models.Role;
import com.spring.quoctrong.models.User;
import com.spring.quoctrong.repositories.RoleRepository;
import com.spring.quoctrong.repositories.UserRepository;
import com.spring.quoctrong.services.ImgStorageServiceImpl;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	private ImgStorageServiceImpl storageService;
	@Autowired
	RoleRepository roleRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	};
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	};
	
	public User save(User updatedUser, MultipartFile avatar) throws Exception{
		//Lấy thông tin user hiện hành
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user =(CustomUserDetails) authentication.getPrincipal();	
		//nếu file avatar khác null
		if (avatar != null && !avatar.isEmpty()) {
			String generatedName = storageService.save(avatar);
			user.getUser().setAvatar(generatedName);
		} else {
			//user.getUser().setAvatar(null);
		}
		//cập nhật các thông tin cho phép 
		user.getUser().setAddress(updatedUser.getAddress());
		user.getUser().setFullname(updatedUser.getFullname());
		user.getUser().setNumberphone(updatedUser.getNumberphone());	
		return userRepository.save(user.getUser());
	}
	
	
	public User updateRole(Integer userId, Integer roleId) throws Exception{
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Người dùng không tồn tại!"));
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new Exception("Vai trò không tồn tại!"));
		
		Set<Role> roles = new HashSet<>();
		Set<User> users = new HashSet<>();
		users.add(user);
		roles.add(role);
		user.setRoles(roles);
		role.setUsers(users);
		roleRepository.save(role);
		return userRepository.save(user);
	}
	
	public User updateEnable(Integer userId, Integer enable) throws Exception{
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Người dùng không tồn tại!"));
		if (enable == 1) {
			user.setEnabled(true);
		}else {
			if (enable == 0) {
				user.setEnabled(false);
			}
		}
		return userRepository.save(user);
	}
}
