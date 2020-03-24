package com.promise.exceptions;

public class UsernameAlreadyExistExceptionResponse {
  private String error;
  
  public UsernameAlreadyExistExceptionResponse(String error) {
	  this.error = error;
  }

public String getError() {
	return error;
}

public void setError(String error) {
	this.error = error;
}
  
  
}
