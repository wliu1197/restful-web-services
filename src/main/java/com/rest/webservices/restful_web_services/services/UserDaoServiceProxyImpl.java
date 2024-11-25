package com.rest.webservices.restful_web_services.services;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rest.webservices.restful_web_services.exception.UserNotFoundException;
import com.rest.webservices.restful_web_services.model.UserDetails;

@Service
@Qualifier("UserDaoServiceProxy")
public class UserDaoServiceProxyImpl implements UserDaoAgent {
	private static List<UserDetails> users = Stream.of(
			new UserDetails(1, "wen", LocalDate.now().minusYears(20),10),
			new UserDetails(2, "hao", LocalDate.now().minusYears(30),20),
			new UserDetails(3, "hao", LocalDate.now().minusYears(40),30))
			.collect(Collectors.toList()); 

	@Override
	public List<UserDetails> findAll(){		
		return users;
	}
	
	@Override
	public UserDetails findById(int id) {
		Predicate<UserDetails> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	@Override
	public UserDetails findActiveUserByName(String name) {
		return users.stream().filter(user-> name.equalsIgnoreCase(user.getName())).findFirst().get();
	}
	
	@Override
	public UserDetails saveUser(UserDetails user) {
		if(!users.isEmpty()) {
			user.setId(users.size()+1);
		}else {
			user.setId(1);
		}
		users.add(user);
		return user;
	}
	
	@Override
	public void deleteById(int id) {
		if(findById(id) == null ) {
			throw new UserNotFoundException(String.format("Can't find user by id: %s", id));
		}
		Predicate<UserDetails> predicate = user -> user.getId().equals(id);
		users.removeIf(predicate);
	}
	
	@Override
	public String getAuthenticatedUser() {
		Authentication autthentication =  SecurityContextHolder.getContext().getAuthentication();
		return autthentication.getName();
	}
	
	@Override
	public int getTotalNumberOfPostsByUserDetails(UserDetails user) {
		return 0;
	}
	
	@Override
	public UserDetails findUserWithBiggestTodoAmount() {
		return null;
	}
}
