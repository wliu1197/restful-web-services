package com.rest.webservices.restful_web_services.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
/*
In this example, we’re creating a CustomBasicAuthEntryPoint class that extends BasicAuthenticationEntryPoint.
We’re overriding the commence() method to customize the response format and the afterPropertiesSet() method to set the realm name.
*/
@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=" +getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        //return customized error message when Auth failed
        //HTTP Status 401 - Unauthorized Full authentication is required to access this resource
        writer.println("HTTP Status 401 - Unauthorized " + authEx.getMessage());
        
    }

    @Override
    public void afterPropertiesSet()  {
        setRealmName("Your Realm Name");
        super.afterPropertiesSet();
    }
}