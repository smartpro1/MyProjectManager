package com.promise.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.promise.exceptions.ProjectIdException;
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
         
		 saveUser(user);
		 return user;
		
		 
	}
	
	public void saveUser(User user) {
		 userRepo.save(user);
	}
	
	public User checkUserByEmail(String email) {
		User checkUser = userRepo.findByUsername(email);
		if(checkUser == null) {
			throw new ProjectIdException("it appears you do not have an account with us");
		}
		return checkUser;
	}
	
	public String generatePasswordResetToken() {
		String passResetToken = UUID.randomUUID().toString();
		return passResetToken;
	}
}
