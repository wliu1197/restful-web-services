package com.rest.webservices.restful_web_services.services;

import com.rest.webservices.restful_web_services.model.TodoDetails;
import com.rest.webservices.restful_web_services.model.UserDetails;

public interface TodoDaoAgent {
	TodoDetails saveTodo(TodoDetails todo); 
	void deleteTodo(int id);
}
