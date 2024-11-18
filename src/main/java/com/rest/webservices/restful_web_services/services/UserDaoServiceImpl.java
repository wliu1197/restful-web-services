package com.rest.webservices.restful_web_services.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rest.webservices.restful_web_services.exception.UserNotFoundException;
import com.rest.webservices.restful_web_services.model.UserDetails;
import com.rest.webservices.restful_web_services.repositories.UserDetailsRepository;

@Service
@Qualifier("UserDaoService")
public class UserDaoServiceImpl implements UserDaoAgent{
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	public void setUserDetailsRepository(UserDetailsRepository userDetailsRepository) {
		this.userDetailsRepository = userDetailsRepository;
	}
	
	
	@Override
	public List<UserDetails> findAll(){
		return userDetailsRepository.findAllActiveUser();
	}
	
	@Override
	public UserDetails findById(int id) {
		return userDetailsRepository.findActiveUserById(id);
	}
	
	@Override
	public UserDetails saveUser(UserDetails user) {
		return userDetailsRepository.save(user);
	}
	
	@Override
	public void deleteById(int id) {
		if(findById(id) == null ) {
			throw new UserNotFoundException(String.format("Can't find user by id: %s", id));
		}
		userDetailsRepository.deleteById(id);
	}
	
	@Override
	public String getAuthenticatedUser() {
		Authentication autthentication =  SecurityContextHolder.getContext().getAuthentication();
		return autthentication.getName();
	}
	
	@Override
	public int getTotalNumberOfPostsByUserDetails(UserDetails user) {
		if(user.getPostDetailsList() == null) return 0;
		return user.getPostDetailsList().size();
	}
	
	

	
	/*
	 * Example of using restTemplate to post request body
	 */
	/*
    @Autowired
    ObjectMapper objectMapper;
    public void restTemplateExample() throws JsonProcessingException {
        String createPersonUrl = "http://localhost:8080/spring-rest/createPerson";
        String updatePersonUrl = "http://localhost:8080/spring-rest/updatePerson";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("id", 1);
        personJsonObject.put("name", "John");


        HttpEntity<String> request =
                new HttpEntity<String>(personJsonObject.toString(), headers);

        //The postForObject() method returns the response body as a String type.
        String personResultAsJsonStr =
                restTemplate.postForObject(createPersonUrl, request, String.class);
        JsonNode root = objectMapper.readTree(personResultAsJsonStr);
        
        //Compared to postForObject(), postForEntity() returns the response as a ResponseEntity object. Other than that, both methods do the same job.

        //Similar to the postForObject, postForEntity has the responseType parameter to convert the response body to the requested Java type.

        //Here, we were able to return the response body as a ResponseEntity<String>.

        //We can also return the response as a ResponseEntity<Person> object by setting the responseType parameter to Person.class:
        HttpEntity<String> request = 
        	      new HttpEntity<String>(personJsonObject.toString(), headers);
        	    
	    ResponseEntity<String> responseEntityStr = restTemplate.
	    				postForEntity(createPersonUrl, request, String.class);
	    JsonNode root = objectMapper.readTree(responseEntityStr.getBody());
        
    }
    */
}
