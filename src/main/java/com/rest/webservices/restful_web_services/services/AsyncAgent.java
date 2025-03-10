package com.rest.webservices.restful_web_services.services;

import java.util.concurrent.CompletableFuture;

public interface AsyncAgent {
    CompletableFuture<Object> calculatePlus(int a, int b);
}
