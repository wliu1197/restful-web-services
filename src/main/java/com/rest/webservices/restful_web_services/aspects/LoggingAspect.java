package com.rest.webservices.restful_web_services.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.query.NativeQuery.ReturnableResultNode;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;


@Configuration
@Aspect
public class LoggingAspect {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired(required = false)
    private HttpServletRequest request;
	
	// pointcut -when we execute this log function
	// execution(* PACKAGE.*.*(..)) - Which class and method we want to execute log
	// execution(* com.spring.aop.spring_aop.business.*.*(..)) - all classes and methods in business package
	// @Before execute this point cut before method is call
	
	@Before("execution(* com.rest.webservices.restful_web_services.controllers.*.*(..))")
	public void logBusinessPackageMethodCall(JoinPoint joinPoint) {
		logger.info("@before Aspect - logging uri {},  Method {} called, args: {}", request.getRequestURL(), joinPoint, joinPoint.getArgs());
	}
	
	// trigger aspect function on @annotation requestMapping level
	// define a point cut on postMapping,putMapping,getMapping,deleteMapping,patchMapping
	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postAction() {}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping(){}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping(){}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMapping(){}
	@Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchMapping(){}
	
	// before post method get call we print uri + payload
	@Before("postAction()")
    public void logAction(JoinPoint joinPoint) {
        String payload = getPayload(joinPoint);
        logger.info("@Before post action: " + request.getRequestURL() + " Payload " + payload);
    }
	
	
	private String getPayload(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            String parameterName = signature.getParameterNames()[i];
            builder.append(parameterName);
            builder.append(": ");
            builder.append(joinPoint.getArgs()[i].toString());
            builder.append(", ");
        }
        return builder.toString();
    }
	
}
