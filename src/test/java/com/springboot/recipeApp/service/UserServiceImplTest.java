package com.springboot.recipeApp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.springboot.recipeApp.exception.UserAlreadyExistsException;
import com.springboot.recipeApp.exception.UserNotFoundException;
import com.springboot.recipeApp.model.Recipe;
import com.springboot.recipeApp.model.User;
import com.springboot.recipeApp.repository.UserRepository;
@SpringBootTest
class UserServiceImplTest {

	@MockBean
	private UserRepository userRepository;
	
	private UserServiceImpl userServiceImpl;
	
	User user;
	Recipe recipe;
	
	@Autowired
	public UserServiceImplTest(UserServiceImpl userServiceImpl) 
	{
		this.userServiceImpl = userServiceImpl;
	}
	
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
	void testSaveUserSuccess() throws UserAlreadyExistsException 
	{
		when(userRepository.getOne(user.getUserId())).thenReturn(null);
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(true, userServiceImpl.saveUser(user));
		
	}
	
	@Test
	void testSaveUserFailure() throws UserAlreadyExistsException 
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.save(user)).thenReturn(null);
		Boolean status;
		try
		{
			status = userServiceImpl.saveUser(user);
		}
		catch(Exception exception)
		{
			status = false;
		}
		assertEquals(false, status);
	}
	
	@Test
	void testFindByEmailSuccess() throws UserNotFoundException
	{
		when(userRepository.getOne(user.getUserId())).thenReturn(user);
		assertEquals(user, userServiceImpl.findByEmail(user.getUserId()));
	}
	
	@Test
	void testFindByEmailFailure() 
	{
		when(userRepository.getOne(user.getUserId())).thenReturn(null);
		User demoUser;
		try
		{
			demoUser = userServiceImpl.findByEmail(user.getUserId());
		}
		catch(Exception exception)
		{
			demoUser = null;
		}
		assertEquals(null, demoUser);
	}
	
	@Test
	void testSaveOrUpdateUserSuccess() throws UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		user.setPassword("123456");
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(true, userServiceImpl.saveOrUpdateUser(user));
	}
	
	@Test
	void testSaveOrUpdateUserFailure() {
		when(userRepository.findById(user.getUserId())).thenReturn(null);
		user.setPassword("123456");
		when(userRepository.save(user)).thenReturn(null);
		Boolean demoUser;
		try 
		{
			demoUser = userServiceImpl.saveOrUpdateUser(user);
		}
		catch (Exception e)
		{
			demoUser = false;
		}
		assertEquals(false, demoUser);
	}
	
	@Test
	void testAddToFav() throws UserNotFoundException 
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		assertEquals(Collections.singletonList(recipe), userServiceImpl.addToFav(recipe, user.getUserId()));
	}
	
	@Test
	void testAddtoRecommend() throws UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		assertEquals(Collections.singletonList(recipe), userServiceImpl.addToRecommend(recipe, user.getUserId()));
	}
	
	@Test
	void testRemoveFromRecommend() throws UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		userServiceImpl.addToRecommend(recipe, user.getUserId());
		assertEquals(true, userServiceImpl.removeFromRecommend(recipe.getId(), user.getUserId()));
	}
	
	@Test
	void testRemoveFromFav() throws UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		userServiceImpl.addToFav(recipe, user.getUserId());
		assertEquals(true, userServiceImpl.removeFromFav(recipe.getId(), user.getUserId()));
	}
	
	@Test
	void testGetFavByUserId() throws UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		userServiceImpl.addToFav(recipe, user.getUserId());
		assertEquals(Collections.singletonList(recipe), userServiceImpl.getFavByUserId(user.getUserId()));
	}
	
	@Test
	void testGetRecommendByUserId() throws UserNotFoundException
	{
		when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		userServiceImpl.addToRecommend(recipe, user.getUserId());
		assertEquals(Collections.singletonList(recipe), userServiceImpl.getRcmdByUserId(user.getUserId()));
	}

}
