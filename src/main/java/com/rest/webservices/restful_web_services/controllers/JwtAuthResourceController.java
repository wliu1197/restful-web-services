package com.rest.webservices.restful_web_services.controllers;


import java.time.Instant;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.webservices.restful_web_services.model.JwtResponse;


@RestController
@RequestMapping("/jwt")
public class JwtAuthResourceController {
	
	private JwtEncoder jwtEncoder;
	
	public JwtAuthResourceController(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	@PostMapping(path = "/authenticate")
	public JwtResponse authenticate(Authentication authentication) {
		JwtClaimsSet claims= JwtClaimsSet.builder()
								.issuer("self")
								.issuedAt(Instant.now())
								.expiresAt(Instant.now().plusSeconds(60 * 60))
								.subject(authentication.getName())
								.claim("roles", getScope(authentication))
								.build();
		JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);
		String token = jwtEncoder.encode(parameters).getTokenValue();
		
		return new JwtResponse(token);
	}
	
	@PostMapping(path = "/authenticate-info")
	public Authentication authenticateInfo(Authentication authentication) {
		return authentication;
	}
	
	private String getScope(Authentication authentication) {
		return authentication.getAuthorities().stream()
					.map(a -> a.getAuthority())
					.collect(Collectors.joining(" "));
	}
}
