package com.rest.webservices.restful_web_services.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.*;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.rest.webservices.restful_web_services.services.UserDaoAgent;

import jakarta.activation.DataSource;
import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;


@Configuration
public class SecurityConfigurationJWTAuth {
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
				.roles("tester")
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
		//List<UserDetails> users = getUsers();
		return new InMemoryUserDetailsManager(users);
	}
	/***************** JWT settings start ***********************/
	
	//Step 1:generate RSA keypair (public key/ private key)
	@Bean
	public KeyPair keyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); //key size 2048 bit RSA encryption
		return keyPairGenerator.generateKeyPair();
	}
	
	//Step 2:generate RSAKey object
	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		System.out.println("public key: " + (RSAPublicKey) keyPair.getPublic());
		System.out.println("private key: " + (RSAPrivateKey) keyPair.getPrivate());
		
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
					.privateKey(keyPair.getPrivate())
					.keyID(UUID.randomUUID().toString())
					.build();
	}
	
	//Step 3:
	//generate JWKSource by JWKSet
	//generate JWKSet by RSAKey
	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector,context) -> jwkSelector.select(jwkSet);
	}
	
	//Step 4: generate JWTDecoder
	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder
				.withPublicKey(rsaKey.toRSAPublicKey())
				.build();
	}
	
	//Step 5: generate JWTEncoder
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
	
	//Step 6: use JwtAuthenticationConverter to fetch authentication authority from token claim
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		grantedAuthoritiesConverter.setAuthorityPrefix("");
		
		final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}
	/***************** JWT settings end ***********************/
	
	/***************** Basic Auth generate token filterChain ***********************/
	
	//only /jwt/** will use basic Auth to generate token
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Bean
	public SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
		// 1 .cros().and() to allow cross-origin resource sharing so rest api can accept request from our react app
		// in RestfulWebServicesApplication class I have added WebMvcConfigurer corsConfigurer() to configure that cros
		// 2 disable csrf(Cross-Site Request Forgery) so we can allow post or put request 
		//	 as we are using STATELESS not seesion use to store credential info
		// 3 basic auth with session policy stateless
		// 4 configure requests to be authenticated for uri path and user role configured in UserDetailsManage
		http
		.cors().and()
		.csrf(csrf -> csrf.disable())
		.securityMatcher("/jwt/**")
		.authorizeHttpRequests(authz -> 
					authz.requestMatchers("/jwt/**").hasRole("developer")
        )
		.httpBasic(Customizer.withDefaults())
		.sessionManagement(sess -> 
			sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.exceptionHandling(ex-> ex.authenticationEntryPoint(authEntryPoint));
		return http.build();		
	}
	
	
	/***************** JWT filterChain ***********************/
	
	//JWT token can access below uri
	@Bean
	public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
		// 1 .cros().and() to allow cross-origin resource sharing so rest api can accept request from our react app
		// in RestfulWebServicesApplication class I have added WebMvcConfigurer corsConfigurer() to configure that cros
		// 2 disable csrf(Cross-Site Request Forgery) so we can allow post or put request 
		//	 as we are using STATELESS not seesion use to store credential info
		// 3 jwt auth with session policy stateless
		// 4 configure requests to be authenticated for uri path and authority users from token by JwtAuthenticationConverter
		http
		.cors().and()
		.csrf(csrf -> csrf.disable())
		.sessionManagement(sess -> 
					sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(authz -> 
		// ################ AWS Elastic beanstalk related configuration #########################
		// allowed access to root URI so ELB can do health check
					authz.requestMatchers("/").permitAll()
		// ################ AWS Elastic beanstalk related configuration #########################
					.requestMatchers("/health").permitAll()
					.requestMatchers("/rest/**").hasAnyAuthority("ROLE_developer")
					.anyRequest().authenticated()
        )
		.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
		.exceptionHandling(ex-> ex.authenticationEntryPoint(authEntryPoint));
	
		return http.build();		
		
	
	}
	
	
	
	
	
}
