package com.rest.webservices.restful_web_services.services;

import java.util.List;

import com.rest.webservices.restful_web_services.model.UserDetails;

public interface UserDaoAgent {
	List<UserDetails> findAll();
	UserDetails findById(int id);
	UserDetails findActiveUserByName(String name);
	UserDetails saveUser(UserDetails user);
	void deleteById(int id);
	String getAuthenticatedUser();
	int getTotalNumberOfPostsByUserDetails(UserDetails user);
}
