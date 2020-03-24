package com.promise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.promise.models.User;
import com.promise.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired 
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if(username == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return user;
	}
	
	@Transactional
	public User loadUserById(Long id) {
		User user = userRepo.getUserById(id);
		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return user;
		
	}

}
