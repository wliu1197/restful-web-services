package com.rest.webservices.restful_web_services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.webservices.restful_web_services.model.TodoDetails;

@Repository
public interface TodoDetailsRepository extends JpaRepository<TodoDetails, Integer>{

}
