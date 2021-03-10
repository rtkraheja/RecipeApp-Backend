package com.springboot.recipeApp.controller;


import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.recipeApp.exception.UserNotFoundException;
import com.springboot.recipeApp.model.User;
import com.springboot.recipeApp.service.UserService;






@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins="http://localhost:4200",allowedHeaders="*",allowCredentials="true")
public class UserAuthenticationController {

	
	
	UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserAuthenticationController(UserService userService) {
		this.userService = userService;
	}


	
	
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user,HttpSession session)  {
		HttpHeaders headers = new HttpHeaders();
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		ResponseEntity<?> res = null;
		try {
			
			User us = userService.findByEmail(user.getUserId());
			String pass= us.getPassword();
			
			System.out.println(pass);
			System.out.println(user.getPassword());
			if(passwordEncoder.matches(user.getPassword(),pass)) {
				
				session.setAttribute("loggedInUserId", user.getUserId());
				res = new ResponseEntity<User>(us,headers, HttpStatus.OK);
			}
			
			else {
				res = new ResponseEntity<String>("invalid password",headers, HttpStatus.UNAUTHORIZED);
			}
			
			
		} catch (Exception e) 
		{
			res = new ResponseEntity<String>("user not found",headers, HttpStatus.UNAUTHORIZED);
		} 
		return res;	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	@GetMapping(value = "/logout")
	public ResponseEntity<String> logout(HttpSession session) {
		HttpHeaders headers = new HttpHeaders();
		try {
			if (session.getAttribute("loggedInUserId") != null) {
				session.removeAttribute("loggedInUserId");
				return new ResponseEntity<>(headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			throw e;
		}
	}
}
