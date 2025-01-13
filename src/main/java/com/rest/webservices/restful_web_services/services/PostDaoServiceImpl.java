package com.rest.webservices.restful_web_services.services;

import com.rest.webservices.restful_web_services.exception.UserNotFoundException;
import com.rest.webservices.restful_web_services.model.UserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.rest.webservices.restful_web_services.model.PostDetails;
import com.rest.webservices.restful_web_services.repositories.PostDetailsRepository;
import com.rest.webservices.restful_web_services.repositories.UserDetailsRepository;

@Service
@Qualifier("PostDaoService")
public class PostDaoServiceImpl implements PostDaoAgent{
	private UserDaoAgent userAgent;
	private PostDetailsRepository repository;

	//put this function in transaction
	//flush() function can immediatly save/update inside transaction otherwise save/update will happen in the end
	@Transactional
	@Override
	public PostDetails createPostByUserId(int id,PostDetails postsDetails){
		UserDetails userDetails = userAgent.findById(id);
		if(userDetails == null) {
			throw new UserNotFoundException("Can't find user by id:" + id);
		}
		//make sure even client pass id field in
		//we reset it to null so spring data jpa will create new one for us
		postsDetails.setId(null);
		postsDetails.setUserDetails(userDetails);
		return savePost(postsDetails);
	}
	
	@Autowired
	public void setPostDetailsRepository(PostDetailsRepository repository) {
		this.repository = repository;
	}
	@Autowired
	public void setUserAgent(@Qualifier("UserDaoService")UserDaoAgent userAgent) {
		this.userAgent = userAgent;
	}

	public PostDetails savePost(PostDetails post) {
		return repository.save(post);
	}
}
