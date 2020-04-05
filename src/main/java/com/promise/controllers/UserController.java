package com.promise.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.promise.models.User;
import com.promise.payload.ChangePasswordRequest;
import com.promise.payload.JWTLoginSuccessResponse;
import com.promise.payload.LoginRequest;
import com.promise.payload.PasswordResetRequest;
import com.promise.security.JwtTokenProvider;
import com.promise.services.EmailService;
import com.promise.services.ProjectService;
import com.promise.services.UserService;
import com.promise.validator.ChangePasswordValidator;
import com.promise.validator.UserValidator;
import static com.promise.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private UserValidator userValidator;
    
	@Autowired
	private ChangePasswordValidator changePasswordValidator;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired 
    private ProjectService projectService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		// compare password
				userValidator.validate(user, result);
		if(result.hasErrors()) return projectService.validateError(result);
		
		User newUser = userService.registerUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
		
		if(result.hasErrors()) return projectService.validateError(result);
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
		
		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
	}
	
	
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest, BindingResult result,  
			HttpServletRequest httpServletRequest){
		
		if(result.hasErrors()) return projectService.validateError(result);
		String userEmail = passwordResetRequest.getEmail();
		System.out.println(userEmail);
		User user = userService.checkUserByEmail(userEmail);
		user.setResetToken(userService.generatePasswordResetToken());
		userService.saveUser(user);
		// something like this : https://mywebapp.com/reset?token=9e5bf4a8-66b8-433e-b91c-6382c1a25f00
		String appUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName();
		
		// Email message
		SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
		passwordResetEmail.setFrom("smartpromise380@gmail.com");
		passwordResetEmail.setTo(userEmail);
		passwordResetEmail.setSubject("Password Reset Request");
		passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + ":3000/reset?token=" +user.getResetToken());
		emailService.sendEmail(passwordResetEmail);
			
		return new ResponseEntity<String>("reset password mail sent to " +userEmail, HttpStatus.OK);		
		
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, BindingResult result){
		// compare password
		changePasswordValidator.validate(changePasswordRequest, result);
        if(result.hasErrors()) return projectService.validateError(result);
        userService.changePassword(changePasswordRequest.getPassword(), changePasswordRequest.getToken());
        
        return new ResponseEntity<String>("password reset successful", HttpStatus.OK);
	}

}
