package com.rest.webservices.restful_web_services.exception;

import java.util.Date;


public class CustomErrorDetails {
	//timestamp
	//message
	//details
	private Date timestamp;
	private String message;
	private String details;
	public CustomErrorDetails(String message, String details) {
		super();
		this.message = message;
		this.details = details;
		this.timestamp = new Date();
	}
	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDetails() {
		return details;
	}
	
	
}
