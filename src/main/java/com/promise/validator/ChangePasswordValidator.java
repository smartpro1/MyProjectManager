package com.promise.validator;



import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.promise.payload.ChangePasswordRequest;

@Component
public class ChangePasswordValidator implements Validator{
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return ChangePasswordRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ChangePasswordRequest changePasswordRequest = (ChangePasswordRequest) target;
		if(changePasswordRequest.getPassword().length() < 6) {
			errors.rejectValue("password", "Length", "must be at least 6 characters");
		}
		
		if(!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())) {
			System.out.println(changePasswordRequest.getPassword());
			System.out.println(changePasswordRequest.getConfirmPassword());
			errors.rejectValue("confirmPassword", "Match", "passwords must match");
		}
		
	}
}
