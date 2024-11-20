package com.rest.webservices.restful_web_services.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class SecurityConfigurationBasicAuth {
	//Customized response message when Auth failed
	private AuthenticationEntryPoint authEntryPoint;
	
	@Autowired
	public void setAuthEntryPoint(AuthenticationEntryPoint authEntryPoint) {
		this.authEntryPoint = authEntryPoint;
	}

	@Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	//Configure Basic auth users and keep them in memory user InMemoryUserDetailsManager class
	//We can get auth from db easily in this way
	@Bean
	public InMemoryUserDetailsManager createDetailsManager() {
		//users we can get it from DB By repository
		List<UserDetails> users = new ArrayList<UserDetails>();
		UserDetails developer = User.builder()
				.username("wen")
				.password(encoder().encode("comein22"))
				.roles("developer")
				.build();
		
		UserDetails tester = User.builder()
				.username("test")
				.password(encoder().encode("comein22"))
				.roles("tester")
				.build();
		
			
		users.add(developer);
		users.add(tester);
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
		.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(authz -> authz
	            .requestMatchers("/rest/**").hasRole("developer") //user developer can access /rest/
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
