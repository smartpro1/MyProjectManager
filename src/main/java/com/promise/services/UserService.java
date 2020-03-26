package com.promise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.promise.exceptions.UsernameAlreadyExistException;
import com.promise.models.User;
import com.promise.repositories.UserRepository;

@Service
public class UserService {
  
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User registerUser(User user) {
		User checkUser = userRepo.findByUsername(user.getUsername());
		if(checkUser != null) {
			throw new UsernameAlreadyExistException("username '" + user.getUsername() + "' already exists please choose another username");
		}
		
		 user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		 user.setConfirmPassword("");

		 return userRepo.save(user);
		
		 
	}
}
