package com.spring.quoctrong.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
		private User user;
	
	//-------- Implements các methods của interface UserDetails
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			 Set<GrantedAuthority> authorities = new HashSet<>();
			    for (Role role : user.getRoles()) {
			            authorities.add(new SimpleGrantedAuthority(role.getName()));
			    }
	
		return authorities;
		}

		@Override
		public String getPassword() {
			return user.getPassword();
		}

		@Override
		public String getUsername() {
			return user.getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			
			return true;
		}

		@Override
		public boolean isEnabled() {
			
			return this.user.isEnabled();
		}
		
}
