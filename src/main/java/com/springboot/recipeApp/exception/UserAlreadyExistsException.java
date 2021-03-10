package com.springboot.recipeApp.exception;

public class UserAlreadyExistsException extends Exception {

	public UserAlreadyExistsException(String message) {
        super(message);
	}
}
