package com.springboot.recipeApp.service;

import java.util.List;

import com.springboot.recipeApp.exception.UserAlreadyExistsException;
import com.springboot.recipeApp.exception.UserNotFoundException;
import com.springboot.recipeApp.model.Recipe;
import com.springboot.recipeApp.model.User;

public interface UserService {
	
	public User findByEmail(String email) throws UserNotFoundException;
	
	public boolean saveUser(User user) throws UserAlreadyExistsException;
	
	public boolean saveOrUpdateUser(User user) throws UserNotFoundException;
	
	/*public boolean addToFav (Recipe recipe, String userId) throws UserNotFoundException;*/
	
	public List<Recipe> addToFav (Recipe recipe, String userId) throws UserNotFoundException;
	
	
//	public boolean addToRecommend (Recipe recipe, String userId) throws UserNotFoundException;
	
	public List<Recipe> addToRecommend (Recipe recipe, String userId) throws UserNotFoundException;
	
	public boolean removeFromRecommend (Integer recipeId, String userId) throws UserNotFoundException;
	public boolean removeFromFav (Integer recipeId, String userId) throws UserNotFoundException;
	
	
	public List<Recipe> getFavByUserId (String userId) throws UserNotFoundException;
    
	public List<Recipe> getRcmdByUserId (String userId) throws UserNotFoundException;


}
