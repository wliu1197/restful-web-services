package com.rest.webservices.restful_web_services.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.rest.webservices.restful_web_services.model.HelloWorldBean;
import com.rest.webservices.restful_web_services.model.UserDetails;
import com.rest.webservices.restful_web_services.services.UserDaoAgent;

@RestController
@RequestMapping("/test")
public class HelloWorldController {
	
	@Value("${say.hello.to.name}")
	public String name;
	
	//when there are more than one implementation of UserDaoAgent interface
	//user of qualifier to specify which service we are inject the dependency
	@Autowired
	//@Qualifier("UserDaoProxy")
	@Qualifier("UserDaoService")
	UserDaoAgent useragent;
	
	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "Hello World " + name; 
	}
	
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}
	
	@GetMapping(path = "/hello-world/name/{name}")
	public HelloWorldBean helloWorldParthVariable(@PathVariable String name) {
		//return new HelloWorldBean("Hello World" + name);
		//or we can use String format
		return new HelloWorldBean(String.format("Hello World %s", name));
	}
	
	@GetMapping(path = "/users")
	public List<UserDetails> getAllUsers() {
		return useragent.findAll();
	}

}
