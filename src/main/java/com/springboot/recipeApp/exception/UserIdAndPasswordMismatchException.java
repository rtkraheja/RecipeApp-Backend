package com.springboot.recipeApp.exception;

public class UserIdAndPasswordMismatchException extends Exception {
	
	public UserIdAndPasswordMismatchException(String message) {
        super(message);
	}
}
