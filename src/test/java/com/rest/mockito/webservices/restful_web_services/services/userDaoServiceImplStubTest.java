package com.rest.mockito.webservices.restful_web_services.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.rest.webservices.restful_web_services.model.UserDetails;
import com.rest.webservices.restful_web_services.repositories.UserDetailsRepository;
import com.rest.webservices.restful_web_services.services.UserDaoServiceImpl;

class userDaoServiceImplStubTest {
	static UserDaoServiceImpl userDaoServiceImpl;
	
	//run before all tests triggers
	@BeforeAll
	static void boforeAll() {
		System.out.println("Before testing set up static params.");
		userDaoServiceImpl = new UserDaoServiceImpl();
		//create a userDetailsRepositoryStub and istead of connecting to db 
		//we hardcode value in findAllActiveUser() function
		UserDetailsRepositoryStub stub = new UserDetailsRepositoryStub();
		//let userDaoServiceImpl using our UserDetailsRepositoryStub for testing
		userDaoServiceImpl.setUserDetailsRepository(stub);
	}
	
	
	@Test
	void findUserWithBiggestTodoAmountTest() {
		UserDetails userDetails =  userDaoServiceImpl.findUserWithBiggestTodoAmount();
		assertEquals("wen", userDetails.getName());
	}
	
	
}
