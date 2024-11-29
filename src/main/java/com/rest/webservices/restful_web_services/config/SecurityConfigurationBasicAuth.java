package com.rest.webservices.restful_web_services.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.*;

import com.rest.webservices.restful_web_services.services.UserDaoAgent;

import jakarta.activation.DataSource;



//@Configuration
public class SecurityConfigurationBasicAuth {

	//Customized response message when Auth failed
	private AuthenticationEntryPoint authEntryPoint;
	
	private UserDaoAgent userDaoAgent;
	
	@Autowired
	public void setAuthEntryPoint(AuthenticationEntryPoint authEntryPoint) {
		this.authEntryPoint = authEntryPoint;
	}

	@Autowired
	public void setUserDaoAgent(@Qualifier("UserDaoService")UserDaoAgent userAgent) {
		this.userDaoAgent = userAgent;
	}

	@Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	private List<UserDetails> getUsersFromDB(){
		List<com.rest.webservices.restful_web_services.model.UserDetails> usersFromDB = userDaoAgent.findAll();
		List<UserDetails> usersInDB = usersFromDB.stream().map(userFromDB -> {
			return User.builder()
					.username(userFromDB.getName())
					.password(encoder().encode(userFromDB.getPassword()))
					.roles(userFromDB.getRole())
					.build();
		}).collect(Collectors.toList());
		return usersInDB;
	}
	
	
	private List<UserDetails> getUsers(){
		List<UserDetails> users = new ArrayList<UserDetails>();
		
		UserDetails developer = User.builder()
				.username("wen")
				.password(encoder().encode("comein22"))
				.roles("developer")
				.build();
		
		UserDetails tester = User.builder()
				.username("anna")
				.password(encoder().encode("comein22"))
				.roles("developer")
				.build();
		
			
		users.add(developer);
		users.add(tester);
		return users;
	}
	
	
	//Configure Basic auth users and keep them in memory user InMemoryUserDetailsManager class
	//We can get auth from db easily by using spring data jpa repository with our userdetail entity
	//lazy to do it so hard code 2 users for now :)
	@Bean
	public InMemoryUserDetailsManager createDetailsManager() {
		List<UserDetails> users = getUsersFromDB();
		return new InMemoryUserDetailsManager(users);
	}
	
	//Override default security filter chain
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 1 .cros().and() to allow cross-origin resource sharing so rest api can accept request from our react app
		// in RestfulWebServicesApplication class I have added WebMvcConfigurer corsConfigurer() to configure that cros
		// 2 disable csrf(Cross-Site Request Forgery) so we can allow post or put request  
		// 3 set basic auth as stateless
		// 4 configure requests to be authenticated for uri path and roles users InMemoryUserDetailsManager
		
		
		http
		.cors().and()
		.csrf(csrf -> csrf.disable())
		.sessionManagement(sess -> 
					sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(authz -> 
					authz.requestMatchers("/rest/**").hasRole("developer") //user developer can access /rest/
	            		 .requestMatchers("/test/**").hasAnyRole("developer","tester")
	            		 .anyRequest().authenticated()
	            //if we want to premit all
	          //.anyRequest().permitAll()
        )
		.httpBasic(Customizer.withDefaults())
		.exceptionHandling(ex-> ex.authenticationEntryPoint(authEntryPoint));
		
		return http.build();		
	}
	
	
	
	
}
