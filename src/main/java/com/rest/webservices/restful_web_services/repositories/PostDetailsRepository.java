package com.rest.webservices.restful_web_services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.webservices.restful_web_services.model.PostDetails;

@Repository
public interface PostDetailsRepository extends JpaRepository<PostDetails, Integer>{

}
