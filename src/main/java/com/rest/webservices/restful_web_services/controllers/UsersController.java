package com.rest.webservices.restful_web_services.controllers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.rest.webservices.restful_web_services.exception.TodoNotFoundException;
import com.rest.webservices.restful_web_services.exception.UserNotFoundException;
import com.rest.webservices.restful_web_services.model.PostDetails;
import com.rest.webservices.restful_web_services.model.TodoDetails;
import com.rest.webservices.restful_web_services.model.UserDetails;
import com.rest.webservices.restful_web_services.repositories.TodoDetailsRepository;
import com.rest.webservices.restful_web_services.services.PostDaoAgent;
import com.rest.webservices.restful_web_services.services.TodoDaoAgent;
import com.rest.webservices.restful_web_services.services.UserDaoAgent;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest")
public class UsersController {
	
	private UserDaoAgent userAgent;
	private PostDaoAgent postAgent;
	private TodoDaoAgent todoAgent;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//dependency injection
	@Autowired
	public UsersController(@Qualifier("UserDaoService")UserDaoAgent userAgent,
			@Qualifier("PostDaoService")PostDaoAgent postAgent
			,TodoDaoAgent todoAgent) {
		this.userAgent = userAgent;
		this.postAgent = postAgent;
		this.todoAgent = todoAgent;
	}
	
	@GetMapping(path = "/basic-auth")
	public ResponseEntity<Void> basicAuthCheckWhenLogin() {
		//print authenticated Basic auth username
		// MOVED this to aspect function
		//logger.info("Basic Auth user: " + userAgent.getAuthenticatedUser());
		
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping(path = "/users")
	public List<UserDetails> getAllUsers() {
		//print authenticated Basic auth username
		logger.info("Basic Auth user: " + userAgent.getAuthenticatedUser());
		return userAgent.findAll();
	}
	/*
	 * 	Apply HATEOAS links provide user more info of API 
	 *	EntityModel and WebMvcLinkBuilder classes are required
	 *
	 *	so in the response we can see extra link to get all users REST API 
	 *	
	    "_links": {
	        "all-users": {
	            "href": "http://localhost:8081/rest/users"
	        }
	    }
	 */
	@GetMapping(path = "/users/{id}")
	public EntityModel<UserDetails> getByUserId(@PathVariable int id) {
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		EntityModel<UserDetails> userEntityModel = EntityModel.of(userDetails);
		WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getAllUsers());
		userEntityModel.add(linkBuilder.withRel("all-users"));
		return userEntityModel;
	}
	
	/* dynamic filtering */
	
	@GetMapping(path = "/users/{id}/name-birthday")
	public ResponseEntity<MappingJacksonValue> getNameAndBirthdayByUserId(@PathVariable int id) {
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userDetails);
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name","birthDate");
		FilterProvider fProvider= new SimpleFilterProvider().addFilter("userParamFilter",filter);
		mappingJacksonValue.setFilters(fProvider);
		return new ResponseEntity<MappingJacksonValue>(mappingJacksonValue, HttpStatus.OK);
	}
	
	
	/* Get by id without HATEOAS link */
	/*
	@GetMapping(path = "/users/{id}")
	public UserDetails getByUserId(@PathVariable int id) {
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		return userDetails;
	}
	*/

	@DeleteMapping(path = "/users/{id}")
	public void deleteByUserId(@PathVariable int id) {
		userAgent.deleteById(id);
	}
	
	
	//user @Valid to validate the user object
	//validateion are configed in UserDetails class by user name @Size and birthDate in the @Past time etc
	@PostMapping(path = "/users")
	public ResponseEntity<UserDetails> createUser(@Valid @RequestBody UserDetails user) {
		 UserDetails userDetails = userAgent.saveUser(user);
		 return new ResponseEntity<UserDetails> (userDetails,HttpStatus.CREATED);
	}
	
	
	@PutMapping(path = "/users/{id}")
	public ResponseEntity<EntityModel<UserDetails>> updateUser(@Valid @RequestBody UserDetails user,@PathVariable(required = true) int id) {
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		//PUT: replace whole user
		user.setId(id);
		userAgent.saveUser(user);
		 
		//add HATEOAS link in response returns getUserById URI so consumer know where to see the updated user details
		//User EntityModel and WebMvcLinkBuilder to add Link
		//userResponseEntity to return response with httpStatus
		EntityModel<UserDetails> userEntityModel = EntityModel.of(userDetails);
		WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getByUserId(user.getId()));
		userEntityModel.add(linkBuilder.withRel("user"));	 
		return new ResponseEntity<EntityModel<UserDetails>> (userEntityModel,HttpStatus.OK);
	}
	
	@PatchMapping(path = "/users/{id}")
	public ResponseEntity<EntityModel<UserDetails>> updateUserPatch(@RequestBody UserDetails user,@PathVariable(required = true) int id) {
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		//PATCH: only update selected params
		if(user.getAge() != null) {
			userDetails.setAge(user.getAge());
		}
		if(user.getBirthDate() != null) {
			userDetails.setBirthDate(user.getBirthDate());
		}
		if(user.getCancelledDate() != null) {
			userDetails.setCancelledDate(user.getCancelledDate());
		}
		if(user.getName() != null) {
			userDetails.setName(user.getName());
		}
		if(user.getPassword()!=null) {
			userDetails.setPassword(user.getPassword());
		}
		userAgent.saveUser(userDetails);		
		//add HATEOAS link in response returns getUserById URI so consumer know where to see the updated user details
		//User EntityModel and WebMvcLinkBuilder to add Link
		//userResponseEntity to return response with httpStatus
		EntityModel<UserDetails> userEntityModel = EntityModel.of(userDetails);
		WebMvcLinkBuilder linkBuilder = linkTo(methodOn(this.getClass()).getByUserId(userDetails.getId()));
		userEntityModel.add(linkBuilder.withRel("user"));	 
		return new ResponseEntity<EntityModel<UserDetails>> (userEntityModel,HttpStatus.OK);
	}
	
	
	
	@GetMapping(path = "/users/{id}/posts")
	public ResponseEntity<List<PostDetails>> getPostsByUserId(@PathVariable int id) {
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		
		return new ResponseEntity<List<PostDetails>> (userDetails.getPostDetailsList(),HttpStatus.OK);
	}
	
	@PostMapping(path = "/users/{id}/posts")
	public ResponseEntity<PostDetails> createPostForUser(@RequestBody PostDetails postsDetails, @PathVariable int id){
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		
		postsDetails.setUserDetails(userDetails);
		postAgent.savePost(postsDetails);
	
		return new ResponseEntity<PostDetails> (postsDetails,HttpStatus.CREATED);
	}
	
	
	@GetMapping(path = "/users/{username}/todos")
	public ResponseEntity<List<TodoDetails>> getTodosByUsername(@PathVariable String username) {
		UserDetails userDetails = userAgent.findActiveUserByName(username);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by username:" + username);
		}
		
		return new ResponseEntity<List<TodoDetails>> (userDetails.getTodoDetailsList(),HttpStatus.OK);
	}
	
	@GetMapping(path = "/users/{username}/todos/{id}")
	public ResponseEntity<TodoDetails> getTodosByUsernameAndId(@PathVariable String username
			,@PathVariable int id) {
		UserDetails userDetails = userAgent.findActiveUserByName(username);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by username:" + username);
		}
		TodoDetails todoDetails = userDetails.getTodoDetailsList()
								.stream().filter(todo-> todo.getId() == id)
								.findFirst()
								.orElse(null);
		if(todoDetails == null) {
			throw new TodoNotFoundException("Can't find user by username:" + username);
		}
		
		return new ResponseEntity<TodoDetails> (todoDetails,HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/users/{username}/todos/{id}")
	public ResponseEntity<Void> deleteTodoByUsernameAndTodoId(@PathVariable String username
			,@PathVariable int id) {
		UserDetails userDetails = userAgent.findActiveUserByName(username);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by username:" + username);
		}
		TodoDetails todoDetails = userDetails.getTodoDetailsList()
								.stream().filter(todo-> todo.getId() == id)
								.findFirst()
								.orElse(null);
		if(todoDetails == null) {
			throw new TodoNotFoundException("Can't find todo for username:" + username + " with Todo id: " + id);
		}
		
		todoAgent.deleteTodo(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(path = "/users/{username}/todos/{id}")
	public ResponseEntity<TodoDetails> updateTodoByUsernameAndTodoId(@PathVariable String username
			,@PathVariable int id ,@RequestBody TodoDetails todoRequest) {
		UserDetails userDetails = userAgent.findActiveUserByName(username);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by username:" + username);
		}
		TodoDetails todoDetails = userDetails.getTodoDetailsList()
								.stream().filter(todo-> todo.getId() == id)
								.findFirst()
								.orElse(null);
		if(todoDetails == null) {
			throw new TodoNotFoundException("Can't find todo for username:" + username + " with Todo id: " + id);
		}
		todoRequest.setUserDetails(userDetails);
		todoRequest.setId(todoDetails.getId());
		todoAgent.saveTodo(todoRequest);
		
		return new ResponseEntity<TodoDetails> (todoRequest,HttpStatus.OK);
	}
	
	@PostMapping(path = "/users/{username}/todos")
	public ResponseEntity<TodoDetails> createTodo(@PathVariable String username
			,@RequestBody TodoDetails todoRequest) {
		UserDetails userDetails = userAgent.findActiveUserByName(username);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by username:" + username);
		}
		
		todoRequest.setUserDetails(userDetails);
		todoAgent.saveTodo(todoRequest);
		
		return new ResponseEntity<TodoDetails> (todoRequest,HttpStatus.OK);
	}
	
	
	
	
	//versioning via URI
	@GetMapping(path = "/v2/users")
	public List<UserDetails> getAllUsersv2() {
		return userAgent.findAll();
	}
	
	//versioning via request params
	@GetMapping(path = "/users/{id}", params="version=2")
	public UserDetails getByUserIdv2(@PathVariable int id) {
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		return userAgent.findById(id);
	}
	
	

}
