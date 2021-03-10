package com.springboot.recipeApp.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

	
	@Id
	private String userId;  /* Email */

	private String firstName;

	private String lastName;

	private String password;

	private String profileImage;
	@OneToMany(cascade=CascadeType.ALL)
	private List<Recipe> favourites;
	@OneToMany(cascade=CascadeType.ALL)
	private List<Recipe> recommendations;
	
	
	
	
	
	

	
	
	
}

	

	
