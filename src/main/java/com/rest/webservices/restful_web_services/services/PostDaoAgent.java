package com.rest.webservices.restful_web_services.services;

import com.rest.webservices.restful_web_services.model.PostDetails;
import com.rest.webservices.restful_web_services.model.UserDetails;

public interface PostDaoAgent {
	PostDetails savePost(PostDetails post);
	PostDetails createPostByUserId(int id,PostDetails postsDetails);
}
