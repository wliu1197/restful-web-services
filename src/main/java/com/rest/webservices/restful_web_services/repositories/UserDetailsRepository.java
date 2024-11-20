package com.rest.webservices.restful_web_services.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rest.webservices.restful_web_services.model.UserDetails;


@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>{
	List<UserDetails> findByName(String name);
	
	@Query("FROM user_details u WHERE u.id = ?1 and u.cancelledDate is null")
	UserDetails findActiveUserById(Integer id);
	
	@Query("FROM user_details u WHERE u.name = ?1 and u.cancelledDate is null")
	UserDetails findActiveUser(String name);
	
	@Query("FROM user_details u WHERE u.cancelledDate is null")
	List<UserDetails> findAllActiveUser();
}
