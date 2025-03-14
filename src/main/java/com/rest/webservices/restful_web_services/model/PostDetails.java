package com.rest.webservices.restful_web_services.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity(name="post_details")
public class PostDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="description")
	@JsonProperty("description")
	private String description;
	
	@ManyToOne
	//if we don't use @JoinColumn annotation data jpa will automatically create user_details_id column for us
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonIgnore
	private UserDetails userDetails;

	public PostDetails() {
		super();
	}
	
	public PostDetails(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	@Override
	public String toString() {
		return "PostDetails [id=" + id + ", description=" + description + "]";
	}
	
	
}
