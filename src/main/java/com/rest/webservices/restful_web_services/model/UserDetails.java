package com.rest.webservices.restful_web_services.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;


//todo: Map this entity to DB later
@Entity (name="user_details")
public class UserDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="name")
	@Size(min=2,message = "Name should has at least 2 characters")
	@JsonProperty("user_name")
	private String name;
	
	@Column(name="birth_date")
	@Past(message = "Birth date should be in the past")
	@NotNull(message = "Birth date should not be null")
	@JsonProperty("birth_date")
	private LocalDate birthDate;
	
	@Column(name="password")
	private String password;
	
	@Column(name="age")
	private Integer age;
	
	@Column(name="cancelled")
	private LocalDate cancelledDate;
	
	@JsonIgnore
	@OneToMany(mappedBy = "userDetails")
	private List<PostDetails> postDetailsList;
	
	public UserDetails() {
		super();
	}
	
	public UserDetails(String name, LocalDate birthDate, Integer age) {
		super();
		this.name = name;
		this.birthDate = birthDate;
		this.age = age;
		this.cancelledDate = null;
	}
	
	public UserDetails(Integer id, String name, LocalDate birthDate, Integer age) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.age = age;
		this.cancelledDate = null;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public LocalDate getCancelledDate() {
		return cancelledDate;
	}
	public void setCancelledDate(LocalDate cancelled) {
		this.cancelledDate = cancelled;
	}

	public List<PostDetails> getPostDetailsList() {
		return postDetailsList;
	}

	public void setPostDetailsList(List<PostDetails> postDetailsList) {
		this.postDetailsList = postDetailsList;
	}

	@Override
	public String toString() {
		return "UserDetails [id=" + id + ", name=" + name + ", birthDate=" + birthDate + ", password=" + password
				+ ", age=" + age + ", cancelledDate=" + cancelledDate + "]";
	}
	
}

