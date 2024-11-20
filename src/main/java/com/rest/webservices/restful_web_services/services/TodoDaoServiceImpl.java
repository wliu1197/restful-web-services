package com.rest.webservices.restful_web_services.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.webservices.restful_web_services.model.TodoDetails;
import com.rest.webservices.restful_web_services.repositories.TodoDetailsRepository;


@Service
public class TodoDaoServiceImpl implements TodoDaoAgent{
	TodoDetailsRepository todoRepository;
	
	@Autowired
	public void setTodoDetailsRepository(TodoDetailsRepository todoRepository) {
		this.todoRepository = todoRepository;
	}
	
	@Override
	public TodoDetails saveTodo(TodoDetails todo) {
		return todoRepository.save(todo);
	}
	
	@Override
	public void deleteTodo(int id) {
		todoRepository.deleteById(id);
	}
}
