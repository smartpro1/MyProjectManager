package com.promise.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.promise.exceptions.ProjectIdException;
import com.promise.exceptions.UsernameAlreadyExistException;
import com.promise.models.PasswordReset;
import com.promise.models.User;
import com.promise.repositories.PasswordResetRepository;
import com.promise.repositories.UserRepository;

@Service
public class UserService {
  
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordResetRepository passwordResetRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User registerUser(User user) {
		User checkUser = userRepo.findByUsername(user.getUsername());
		PasswordReset passwordReset = new PasswordReset();
		if(checkUser != null) {
			throw new UsernameAlreadyExistException("username '" + user.getUsername() + "' already exists please choose another username");
		}
		
		 user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		 user.setConfirmPassword("");
         
		 user.setPasswordReset(passwordReset);
		 passwordReset.setUser(user);
		 User savedUser = userRepo.save(user);
		 return savedUser;
		
		 
	}
	
	public void changePassword(String password, String token) {
		PasswordReset passwordReset = passwordResetRepo.findByResetToken(token);
		User user = userRepo.getById(passwordReset.getUser().getId());
		long timeNow = new Date().getTime();
		long checkExpiration = passwordReset.getExpiryDate() - timeNow;
		if(passwordReset == null || checkExpiration < 1 || user == null) {
			throw new ProjectIdException("invalid token or user");
		}
		user.setPassword(bCryptPasswordEncoder.encode(password));
		saveUser(user);
		passwordReset.setResetToken(null);
		passwordResetRepo.deleteById(passwordReset.getId());
		
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
	
	public PasswordReset generatePasswordResetToken(User user) {
		PasswordReset passwordReset = user.getPasswordReset();
		long validityTime = 60 * 60 * 24 * 1000; // 1 day
		Date now = new Date();
		passwordReset.setExpiryDate(now.getTime() + validityTime);
		String passResetToken = UUID.randomUUID().toString();
		passwordReset.setResetToken(passResetToken);
		passwordResetRepo.save(passwordReset);
		return passwordReset;
	}
}
