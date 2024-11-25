package com.rest.mockito.webservices.restful_web_services.services;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.rest.webservices.restful_web_services.model.TodoDetails;
import com.rest.webservices.restful_web_services.model.UserDetails;
import com.rest.webservices.restful_web_services.repositories.UserDetailsRepository;

public class UserDetailsRepositoryStub implements UserDetailsRepository{
	
	@Override
	public List<UserDetails> findAllActiveUser(){
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
		return userList ;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends UserDetails> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserDetails> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<UserDetails> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserDetails getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetails getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetails getReferenceById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserDetails> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserDetails> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserDetails> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDetails> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDetails> findAllById(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserDetails> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<UserDetails> findById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UserDetails entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends UserDetails> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UserDetails> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<UserDetails> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserDetails> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <S extends UserDetails> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends UserDetails> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends UserDetails> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends UserDetails, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDetails> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetails findActiveUserById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetails findActiveUser(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
