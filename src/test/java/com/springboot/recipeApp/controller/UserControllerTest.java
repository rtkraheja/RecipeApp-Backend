package com.springboot.recipeApp.controller;

//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.recipeApp.helper.FileUploadHelper;
import com.springboot.recipeApp.model.Recipe;
import com.springboot.recipeApp.model.User;
import com.springboot.recipeApp.service.UserServiceImpl;

@WebMvcTest
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserServiceImpl userServiceImpl;
	
	@MockBean
	private FileUploadHelper fileUploadHelper;
	
	@MockBean
	private BCryptPasswordEncoder passwordEncoder;
	
	User user;
	Recipe recipe;
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@BeforeEach
	void setUp() throws Exception 
	{
		user = new User("prince@gl.com","Prince","Shakya","password","img.jpg",null,null);	
		recipe = new Recipe(1,"101","Pizza","Pizza Recipe","pizza.jpg");
	}

	@AfterEach
	void tearDown() throws Exception 
	{
		
	}

	@Test
	void testRegister() {
		
	}

	@Test
	void testUpdate() {
		
	}

	@Test
	void testAddFavourite() throws Exception 
	{
		HashMap<String, Object> session = new HashMap<String, Object>();
		session.put("loggedInUserId", user.getUserId());
		
		List<Recipe> recipes = Collections.singletonList(recipe);
		
		user.setFavourites(recipes);
		
		String jsonContent = mapper.writeValueAsString(recipe);
		
		when(userServiceImpl.addToFav(recipe, user.getUserId())).thenReturn(recipes);
		
		mockMvc.perform(
				post("/api/v1/auth/addFav" )
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonContent)
				.sessionAttrs(session)
				)
					.andExpect(status().isOk());
		
	}

	@Test
	void testDeleteFavourite() throws Exception 
	{
		HashMap<String, Object> session = new HashMap<String, Object>();
		session.put("loggedInUserId", user.getUserId());
		
		user.setFavourites(Collections.singletonList(recipe));
		
//		String jsonContent = mapper.writeValueAsString(recipe);
		
		when(userServiceImpl.removeFromFav(recipe.getId(), user.getUserId())).thenReturn(true);
		
		mockMvc.perform(
				delete("/api/v1/auth/deleteFav/1" )
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.sessionAttrs(session)
				)
					.andExpect(status().isOk());
		
	}

	@Test
	void testAddRecommend() throws Exception 
	{
		
		HashMap<String, Object> session = new HashMap<String, Object>();
		session.put("loggedInUserId", user.getUserId());
		
		List<Recipe> recipes = Collections.singletonList(recipe);
		
		user.setFavourites(recipes);
		
		String jsonContent = mapper.writeValueAsString(recipe);
		
		when(userServiceImpl.addToRecommend(recipe, user.getUserId())).thenReturn(recipes);
		
		mockMvc.perform(
				post("/api/v1/auth/addRecommend" )
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonContent)
				.sessionAttrs(session)
				)
					.andExpect(status().isOk());
		
	}

	@Test
	void testDeleteRecommend() throws Exception 
	{
		
		HashMap<String, Object> session = new HashMap<String, Object>();
		session.put("loggedInUserId", user.getUserId());
		
		user.setFavourites(Collections.singletonList(recipe));
		
	//	String jsonContent = mapper.writeValueAsString(recipe);
		
		when(userServiceImpl.removeFromRecommend(recipe.getId(), user.getUserId())).thenReturn(true);
		
		mockMvc.perform(
				delete("/api/v1/auth/deleteRecommend/1" )
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.sessionAttrs(session)
				)
					.andExpect(status().isOk());
		
	}

}
