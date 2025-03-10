package com.rest.webservices.restful_web_services.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncAgentImpl implements AsyncAgent{
    @Async
    @Override
    public CompletableFuture<Object> calculatePlus(int a, int b){
        try {
            // Simulate a long-running task
            Integer result = 1 + 3; // Your simple calculation
            Thread.sleep(10000); // Simulate delay
            // Return the result as a CompletableFuture
            return CompletableFuture.completedFuture(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(e.getMessage()); // Return null if there's an error
        }
    }
}
