package com.springboot.recipeApp.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springboot.recipeApp.exception.UserAlreadyExistsException;
import com.springboot.recipeApp.exception.UserNotFoundException;
import com.springboot.recipeApp.helper.FileUploadHelper;
import com.springboot.recipeApp.model.Recipe;
import com.springboot.recipeApp.model.User;
import com.springboot.recipeApp.service.UserService;





@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins="http://localhost:4200",allowedHeaders="*",allowCredentials="true")
public class UserController {

	@Autowired
	private FileUploadHelper fileUploadHelper;
	
	UserService userService;

	private static String SESSION_ID = "loggedInUserId";
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/register",
	consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(
			
			@RequestParam("userId") String userId,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("password") String password,
			@RequestParam("profileImage") MultipartFile file) throws IOException {
		
		
		
		
		
		
		User user = new User();
		
		
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPassword(passwordEncoder.encode(password));
		user.setUserId(userId);
		try {
			
			
			if(fileUploadHelper.uploadFile(file)) {
				
				user.setProfileImage(file.getOriginalFilename());
				if(userService.saveUser(user)) {
					
					Map map = new HashMap<String, Object>();
					
				     
					map.put("ImageUrl", ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(file.getOriginalFilename()).toUriString());
					map.put("USER_DATA", user);
					
					return new ResponseEntity<Object>(map, HttpStatus.CREATED);
				}
				
				
			}
			
			
			
		} catch (UserAlreadyExistsException e) {
			return new ResponseEntity<String>("Duplicate User", HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}


	
	@RequestMapping(value = "/update", method = RequestMethod.PUT,
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseEntity<?> update(
					HttpSession session,
					@RequestParam("userId") String userId,
					@RequestParam("firstName") String firstName,
					@RequestParam("lastName") String lastName,
					@RequestParam("password") String password,
					@RequestParam("profileImage") MultipartFile file
					
					) throws IOException { {
						
					}
		HttpHeaders headers = new HttpHeaders();

		if (null != session && null != session.getAttribute(SESSION_ID)) {
			
			  
				
				User user = new User();
				
				
				
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setPassword(passwordEncoder.encode(password));
				user.setUserId(userId);
				
			
			try {
				
				if(fileUploadHelper.uploadFile(file)) {
					
					user.setProfileImage(file.getOriginalFilename());
				if (userService.saveOrUpdateUser(user)) {
					
					Map map = new HashMap<String, Object>();
					
					map.put("ImageUrl", ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(file.getOriginalFilename()).toUriString());
					map.put("USER_DATA", user);
					
					
					return new ResponseEntity<Object>(map, headers, HttpStatus.OK);
				} 
				else 
				{
					return new ResponseEntity<String>("Unable to update user", headers, HttpStatus.NOT_FOUND);
				}
				}
				return new ResponseEntity<String>("Field Required", headers, HttpStatus.UNAUTHORIZED);
			

			} catch (Exception e) {
				return new ResponseEntity<String>("Unable to update user", headers, HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<String>("Unauthorized access", headers, HttpStatus.UNAUTHORIZED);
		}

	}
	
	/*@PostMapping(value = "/addFav")
	public ResponseEntity<?> addFavourite(HttpSession session, @RequestBody Recipe recipe) throws UserNotFoundException
	{
		HttpHeaders headers = new HttpHeaders();
		String userId = (String) session.getAttribute(SESSION_ID);
		System.out.println(userId);
		if(userService.addToFav(recipe, userId))
		{
			return new ResponseEntity<Recipe>(recipe,headers,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Recipe>(recipe,headers,HttpStatus.BAD_REQUEST);
		}
		
	}*/
	
	@PostMapping(value = "/addFav")
	public ResponseEntity<?> addFavourite(HttpSession session, @RequestBody Recipe recipe) throws UserNotFoundException
	{
		HttpHeaders headers = new HttpHeaders();
		String userId = (String) session.getAttribute(SESSION_ID);
		System.out.println(userId);
		int prevSize = userService.getFavByUserId(userId).size();
		List<Recipe> res = userService.addToFav(recipe, userId);
		int currSize = res.size();
		if(res!=null && currSize != prevSize )
		{
			return new ResponseEntity<List<Recipe>>(res,headers,HttpStatus.OK);
		}
		else if(currSize == prevSize)
		{	
			return new ResponseEntity<String>("Already marked as favourite",headers,HttpStatus.CONFLICT);
		}
		else
		{
			return new ResponseEntity<>(headers,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@DeleteMapping(value = "/deleteFav/{recipeId}")
	public ResponseEntity<?> deleteFavourite(HttpSession session, @PathVariable Integer recipeId) throws UserNotFoundException
	{
		
		String userId = (String) session.getAttribute(SESSION_ID);
		System.out.println(userId);
		if(userService.removeFromFav(recipeId, userId))
		{
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		else
		{
			return new ResponseEntity<String>("Failed to remove from favourites",HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/*@PostMapping(value = "/addRecommend")
	public ResponseEntity<?> addRecommend(HttpSession session, @RequestBody Recipe recipe) throws UserNotFoundException
	{
		HttpHeaders headers = new HttpHeaders();
		String userId = (String) session.getAttribute(SESSION_ID);
		if(userService.addToRecommend(recipe, userId))
		{
			return new ResponseEntity<Recipe>(recipe,headers,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Recipe>(recipe,headers,HttpStatus.BAD_REQUEST);
		}
		
	}*/
	
	
	@PostMapping(value = "/addRecommend")
	public ResponseEntity<?> addRecommend(HttpSession session, @RequestBody Recipe recipe) throws UserNotFoundException
	{
		HttpHeaders headers = new HttpHeaders();
		String userId = (String) session.getAttribute(SESSION_ID);
		int prevSize = userService.getRcmdByUserId(userId).size();
		List<Recipe> res = userService.addToRecommend(recipe, userId);
		int currSize = res.size();
		if(res!=null && currSize != prevSize )
		{
			return new ResponseEntity<List<Recipe>>(res,headers,HttpStatus.OK);
		}
		else if(currSize == prevSize)
		{	
			return new ResponseEntity<String>("Already marked as favourite",headers,HttpStatus.CONFLICT);
		}
		else
		{
			return new ResponseEntity<>(headers,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping(value = "/deleteRecommend/{recipeId}")
	public ResponseEntity<?> deleteRecommend(HttpSession session, @PathVariable Integer recipeId) throws UserNotFoundException
	{
		
		String userId = (String) session.getAttribute(SESSION_ID);
		System.out.println(userId);
		if(userService.removeFromRecommend(recipeId, userId))
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Failed to remove from recommendations",HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(value = "/all/fav/{userId}")
	public ResponseEntity<?> favByUserId(HttpSession session, @PathVariable String userId) throws UserNotFoundException
	{
		HttpHeaders headers = new HttpHeaders();
		String uId = (String) session.getAttribute(SESSION_ID);
		System.out.println(userId);
		List<Recipe> fav =userService.getFavByUserId(uId);
		return new ResponseEntity<List<Recipe>>(fav,headers,HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/all/rcmd/{userId}")
	public ResponseEntity<?> rcmdByUserId(HttpSession session, @PathVariable String userId) throws UserNotFoundException
	{
		HttpHeaders headers = new HttpHeaders();
		String uId = (String) session.getAttribute(SESSION_ID);
		System.out.println(userId);
		List<Recipe> rcmd =userService.getRcmdByUserId(uId);
		return new ResponseEntity<List<Recipe>>(rcmd,headers,HttpStatus.OK);
		
	}
	
	
}