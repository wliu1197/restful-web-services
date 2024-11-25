package com.rest.mockito.webservices.restful_web_services.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rest.webservices.restful_web_services.model.TodoDetails;
import com.rest.webservices.restful_web_services.model.UserDetails;
import com.rest.webservices.restful_web_services.repositories.UserDetailsRepository;
import com.rest.webservices.restful_web_services.services.UserDaoServiceImpl;

import io.swagger.v3.oas.annotations.extensions.Extension;

@ExtendWith(MockitoExtension.class)
class userDaoServiceImplMockTest {
	
	@Mock
	UserDetailsRepository userDetailsRepositoryMock;
	
	@InjectMocks
	UserDaoServiceImpl userDaoServiceImpl;
	
	//run before all tests triggers
	@BeforeAll
	static void init() {
		System.out.println("Before testing set up static params.");
		//userDaoServiceImpl = new UserDaoServiceImpl();
	}
	
	
	@Test
	void findUserWithBiggestTodoAmountTest() {
		UserDaoServiceImpl userDaoServiceImpl = new UserDaoServiceImpl();;
		//Instead of create a whole stub class of UserDetailsRepository 
		//then Ovrride it's findAllActiveUser() function
		//We can use Mockito to mock
		UserDetailsRepository userDetailsRepositoryMock = mock(UserDetailsRepository.class);
		when(userDetailsRepositoryMock.findAllActiveUser()).thenReturn(getTestUserList());
		userDaoServiceImpl.setUserDetailsRepository(userDetailsRepositoryMock);
		UserDetails user =  userDaoServiceImpl.findUserWithBiggestTodoAmount();
		assertEquals("wen", user.getName(),"failed on testing result shoulde be wen");
	}
	
	@Test
	void findUserWithBiggestTodoAmountTest_MockAnnotation() {
		//@ExtendWith(MockitoExtension.class) enables @Mock and @InjectMocks features
		//use @Mock create mock
		//@InjectMocks inject all mocks into userDaoServiceImpl
		when(userDetailsRepositoryMock.findAllActiveUser()).thenReturn(getTestUserList());
		UserDetails user =  userDaoServiceImpl.findUserWithBiggestTodoAmount();
		assertEquals("wen", user.getName(),"failed on testing result shoulde be wen");
	}
	
	private List<UserDetails> getTestUserList(){
		UserDetails userDetails = new UserDetails("wen","comein22",LocalDate.of(1990, 1, 8),36);
		
		TodoDetails todo1 = new TodoDetails("learn aws", LocalDate.of(2025, 1, 8), false, userDetails);
		TodoDetails todo2 = new TodoDetails("learn spring", LocalDate.of(2025, 1, 8), false, userDetails);
		List<TodoDetails> wenTodos = new ArrayList<TodoDetails>();
		wenTodos.add(todo1);
		wenTodos.add(todo2);
		userDetails.setTodoDetailsList(wenTodos);
		
		UserDetails userDetails2 = new UserDetails("anna","comein22",LocalDate.of(1990, 1, 8),36);
		TodoDetails todo3 = new TodoDetails("eat more food", LocalDate.of(2025, 1, 8), false, userDetails2);
		List<TodoDetails> annaTodos = new ArrayList<TodoDetails>();
		annaTodos.add(todo3 );
		userDetails2.setTodoDetailsList(annaTodos);
		
		List<UserDetails> userList = new ArrayList<UserDetails>();
		userList.add(userDetails2);
		userList.add(userDetails);
		return userList;
	}
}
