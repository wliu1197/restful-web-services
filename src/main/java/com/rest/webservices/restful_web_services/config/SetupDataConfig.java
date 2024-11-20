package com.rest.webservices.restful_web_services.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import com.rest.webservices.restful_web_services.model.PostDetails;
import com.rest.webservices.restful_web_services.model.TodoDetails;
import com.rest.webservices.restful_web_services.model.UserDetails;
import com.rest.webservices.restful_web_services.services.PostDaoAgent;
import com.rest.webservices.restful_web_services.services.TodoDaoAgent;
import com.rest.webservices.restful_web_services.services.UserDaoAgent;

import jakarta.annotation.PostConstruct;

@Configuration
public class SetupDataConfig {
	
	private UserDaoAgent userAgent;
	private PostDaoAgent postAgent;
	private TodoDaoAgent todoAgent;
	
	@Autowired
    public SetupDataConfig(@Qualifier("UserDaoService")UserDaoAgent userAgent,
				@Qualifier("PostDaoService")PostDaoAgent postAgent,
				TodoDaoAgent todoAgent) {
       this.userAgent = userAgent;
       this.postAgent = postAgent;
       this.todoAgent = todoAgent;
   }
	
	@PostConstruct
	public void setUpSomeTestData() {
		//set up test data in user table
		UserDetails userDetails = new UserDetails("wen","comein22",LocalDate.of(1990, 1, 8),36);
		UserDetails userDetails2 = new UserDetails("anna","comein22",LocalDate.of(1990, 1, 8),36);
		userDetails = userAgent.saveUser(userDetails);
		userDetails2 = userAgent.saveUser(userDetails2);
		
		//set up test data in post table
		PostDetails postsDetails = new PostDetails("wen first message");
		postsDetails.setUserDetails(userDetails);
		postAgent.savePost(postsDetails);
		PostDetails postsDetails2 = new PostDetails("anna first message");
		postsDetails.setUserDetails(userDetails2);
		postAgent.savePost(postsDetails2);
		
		//set up test data in todo table
		TodoDetails todoDetails = new TodoDetails("learn aws", LocalDate.of(2025, 1, 8), false, userDetails);
		TodoDetails todoDetails2 = new TodoDetails("learn spring", LocalDate.of(2025, 1, 8), false, userDetails);
		todoAgent.saveTodo(todoDetails);
		todoAgent.saveTodo(todoDetails2);
		
		TodoDetails annaTodoDetails = new TodoDetails("eat more food", LocalDate.of(2025, 1, 8), false, userDetails2);
		TodoDetails annaTodoDetails2 = new TodoDetails("sleep longer time", LocalDate.of(2025, 1, 8), false, userDetails2);
		todoAgent.saveTodo(annaTodoDetails);
		todoAgent.saveTodo(annaTodoDetails2);
		
	}	
}


