package com.rest.webservices.restful_web_services.controllers;

import com.rest.webservices.restful_web_services.services.AsyncAgent;
import com.rest.webservices.restful_web_services.services.AsyncAgentImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/async")
@RequiredArgsConstructor
public class AsyncController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final AsyncAgent asyncService;
    @PostMapping("/calculate")
    public ResponseEntity<String> processRequest() {
        // Call the asynchronous method
        CompletableFuture<Object> future = asyncService.calculatePlus(1,2);
        future.whenComplete((result,exception) -> {
            if(exception != null){
                logger.error("Failed to send product message: " + exception.getMessage());
            } else {
                logger.info("Successfully sent product message: " + result);
            }
        });

        // Send an immediate response to the client
        return ResponseEntity.ok("Calculation in progress...");
    }
}
