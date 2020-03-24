package com.promise.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promise.models.User;
import com.promise.services.ProjectService;
import com.promise.services.UserService;
import com.promise.validator.UserValidator;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserValidator userValidator;
	
	@Autowired 
	ProjectService projectService;
	
	@PostMapping("/registration")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		// compare password
		userValidator.validate(user, result);
		
		if(result.hasErrors()) return projectService.validateError(result);
		User newUser = userService.registerUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
		
	}

}
