package com.rest.webservices.restful_web_services.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.rest.webservices.restful_web_services.model.PostDetails;
import com.rest.webservices.restful_web_services.repositories.PostDetailsRepository;
import com.rest.webservices.restful_web_services.repositories.UserDetailsRepository;

@Service
@Qualifier("PostDaoServiceProxy")
public class PostDaoServiceProxyImpl implements PostDaoAgent{
		
	
	public PostDetails savePost(PostDetails post) {
		return new PostDetails();
	}
}
