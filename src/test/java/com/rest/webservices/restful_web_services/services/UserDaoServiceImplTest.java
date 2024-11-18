package com.rest.webservices.restful_web_services.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;

import com.rest.webservices.restful_web_services.model.PostDetails;
import com.rest.webservices.restful_web_services.model.UserDetails;

class UserDaoServiceImplTest {
	static UserDaoServiceImpl userDaoServiceImpl;
	static UserDetails user;
	
	//run before all tests triggers
	@BeforeAll
	static void boforeAll() {
		System.out.println("Before testing set up static params.");
		userDaoServiceImpl = new UserDaoServiceImpl();
		user=new UserDetails();
	}
	
	@BeforeEach
	void beforeEach() {
		System.out.println("run this before each test!");
	}

	@Test
	void getTotalNumberOfPostsByUserDetailsTest() {
		System.out.println("Test1");
		PostDetails p1 = new PostDetails();
		PostDetails p2 = new PostDetails();
		PostDetails p3 = new PostDetails();
		ArrayList<PostDetails> postsArrayList = (ArrayList<PostDetails>) Stream.of(p1,p2,p3).collect(Collectors.toList());
		user.setPostDetailsList(postsArrayList);
		int totalPosts =userDaoServiceImpl.getTotalNumberOfPostsByUserDetails(user);
		int expectedPosts = 3;
		assertEquals(expectedPosts, totalPosts, "failed test of getTotalNumberOfPostsByUserDetails");
	}
	
	@Test
	void getTotalNumberOfPostsByUserDetailsWhenNotPostsForUserTest() {
		System.out.println("Test2");
		int totalPosts =userDaoServiceImpl.getTotalNumberOfPostsByUserDetails(user);
		int expectedPosts = 0;
		assertEquals(expectedPosts, totalPosts);
	}
	
	@AfterEach
	void afterEach() {
		System.out.println("run this after each test");
	}

	
	//clean up when all tests finished
	@AfterAll
	static void afterAll() {
		userDaoServiceImpl = null;
		user = null;
		System.out.println("Finished testing.");
	}
	

}
