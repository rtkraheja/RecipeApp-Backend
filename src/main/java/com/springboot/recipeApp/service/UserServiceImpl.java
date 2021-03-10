package com.springboot.recipeApp.service;




import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.recipeApp.exception.UserAlreadyExistsException;
import com.springboot.recipeApp.exception.UserNotFoundException;
import com.springboot.recipeApp.model.Recipe;
import com.springboot.recipeApp.model.User;
import com.springboot.recipeApp.repository.UserRepository;




@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	 
	 @Override
	    public User findByEmail(String email) throws UserNotFoundException {

			User user = userRepo.getOne(email);
			if (user == null) {
				throw new UserNotFoundException("User does not exists.");
			}
			return user;
		}
	@Override
    public boolean saveUser(User user) throws UserAlreadyExistsException  {
    	
		boolean response = false;
		try {
    		User checkUser = userRepo.findById(user.getUserId()).orElse(null);
    		if (checkUser==null ) {
    			userRepo.save(user);
    			response=true;
			}
    		else {
    			throw new UserAlreadyExistsException("User Alreay Registered");	
    		}
    		
		} catch (Exception e) {
			throw new UserAlreadyExistsException("Error Occured");
			
		}
		return response;
		
    }	
	
	@Override
    public boolean saveOrUpdateUser(User user) throws UserNotFoundException  {
    	
		boolean response = false;
		try {
    		User checkUser = userRepo.findById(user.getUserId()).orElse(null);
    		if (checkUser!=null ) {
    			userRepo.save(user);
    			response=true;
			}
    		else {
    			throw new UserNotFoundException("User Not Exists");	
    		}
    		
		} catch (Exception e) {
			throw new UserNotFoundException("Error Occured");
			
		}
		return response;
		
    }
	@Override
	public List<Recipe> addToFav(Recipe recipe, String userId) throws UserNotFoundException {
		// TODO Auto-generated method stub
		boolean response = false;
		boolean exist = false;
		List<Recipe> res = null;
		System.out.println(recipe.getRecipeId() + "--");

		try 
		{
			//System.out.println(userId);
			User user = userRepo.findById(userId).orElse(null);
			List<Recipe> fav = user.getFavourites();
			System.out.println(fav); 
			if(fav.size() != 0)
			{			
				System.out.println("inside if");
				int i=0;
				while(!exist && i<fav.size())
				{
					System.out.println("inside while");
					if(fav.get(i).getRecipeId().equalsIgnoreCase(recipe.getRecipeId()))
						{
							System.out.println("inside if if");
							exist=true;
						}
					i++;
					//System.out.println(i + " " + recipe.getRecipeId() + "--" + fav.get(i).getRecipeId() );
				}
			}
			
			if(!exist)
			{
				
				System.out.println("inner part");
				fav.add(recipe);
				user.setFavourites(fav);
			}
			
			this.saveOrUpdateUser(user);
			response = true;
			res=user.getFavourites();
		}
		catch (Exception e) {
			
			throw new UserNotFoundException("Error Occured");
			
		}
		return res;
		
		
	}
	
	
	@Override
	public List<Recipe> addToRecommend(Recipe recipe, String userId) throws UserNotFoundException {
		// TODO Auto-generated method stub
		System.out.println(userId);
		boolean response = false;
		boolean exist = false;
		List<Recipe> res = null;
		try 
		{
			User user = userRepo.findById(userId).orElse(null);
			System.out.println(user.getUserId());
			List<Recipe> recommend = user.getRecommendations();
			System.out.println(recommend.size());
			if(recommend.size() != 0)
			{			
				System.out.println("inside if");
				int i=0;
				while(!exist && i<recommend.size())
				{
					System.out.println("inside while");
					if(recommend.get(i).getRecipeId().equalsIgnoreCase(recipe.getRecipeId()))
						{
							System.out.println("inside if if");
							exist=true;
						}
					i++;
					//System.out.println(i + " " + recipe.getRecipeId() + "--" + fav.get(i).getRecipeId() );
				}
			}
			
			if(!exist)
			{
				
				System.out.println("inner part");
				recommend.add(recipe);
				user.setRecommendations(recommend);
			}
			//user.setRecommendations(recommend);
			this.saveOrUpdateUser(user);
			response = true;
			res=user.getRecommendations();
		}
		catch (Exception e) {
			
			throw new UserNotFoundException("Error Occured");
			
		}
		return res;
	}
	
	@Override
	public boolean removeFromRecommend(Integer recipeId, String userId) throws UserNotFoundException {
		// TODO Auto-generated method stub
		boolean response = false;
		try 
		{
			User user = userRepo.findById(userId).orElse(null);
			List<Recipe> recommend = user.getRecommendations();
			for(Recipe rec : recommend)
			{
				if(rec.getId() == recipeId)
					{
						recommend.remove(rec);
						break;
					}
			}
			user.setRecommendations(recommend);
			this.saveOrUpdateUser(user);
			response = true;
		}
		catch (Exception e) {
			
			throw new UserNotFoundException("Error Occured");
			
		}
		return response;
	}
	@Override
	public boolean removeFromFav(Integer recipeId, String userId) throws UserNotFoundException {
		// TODO Auto-generated method stub
		boolean response = false;
		try 
		{
			User user = userRepo.findById(userId).orElse(null);
			List<Recipe> fav = user.getFavourites();
			for(Recipe rec : fav)
			{
				if(rec.getId() == recipeId)
					{
						fav.remove(rec);
						break;
					}
			}
			user.setFavourites(fav);
			this.saveOrUpdateUser(user);
			response = true;
		}
		catch (Exception e) {
			
			throw new UserNotFoundException("Error Occured");
			
		}
		return response;
	}
	@Override
	public List<Recipe> getFavByUserId(String userId) throws UserNotFoundException {
		
			User user = userRepo.findById(userId).orElse(null);
			List<Recipe> fav = user.getFavourites();
			return fav;
		
	}
	
	@Override
	public List<Recipe> getRcmdByUserId(String userId) throws UserNotFoundException {
		
			User user = userRepo.findById(userId).orElse(null);
			List<Recipe> rcmd = user.getRecommendations();
			return rcmd;
		
	}
	
	
	
	
    		
    
	
}
