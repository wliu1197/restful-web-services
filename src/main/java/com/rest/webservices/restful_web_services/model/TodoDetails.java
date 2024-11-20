package com.rest.webservices.restful_web_services.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;

@Entity (name="todo_details")
public class TodoDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="description")
	@Size(min=5,message = "at least 5 characters required! ")
	private String description;
	@Column(name="target_date")
	private LocalDate targetDate;
	@Column(name="done")
	private Boolean done;
	
	//if we don't use @JoinColumn annotation data jpa will automatically create user_details_id column for us
	//@JoinColumn(name = "user_id", referencedColumnName = "id")
	@ManyToOne
	@JsonIgnore
	private UserDetails userDetails;
	
	
	public TodoDetails() {
		super();
	}

	public TodoDetails(String description, LocalDate date, boolean done, UserDetails userDetails) {
		super();
		this.description = description;
		this.targetDate = date;
		this.done = done;
		this.userDetails = userDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}

	public Boolean isDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}
	
	public Boolean getDone() {
		return done;	
	}
	
	
	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", description=" + description + ", targetDate=" + targetDate + ", done=" + done
				+ ", user=" + userDetails.getName() + "]";
	}
	
}
