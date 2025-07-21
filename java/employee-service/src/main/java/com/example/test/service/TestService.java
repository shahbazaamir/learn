package com.example.test.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.example.salarylookup.service.EmployeeService;

@Service
public class TestService {

    private final WebClient webClient;
    
     
   

    public TestService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/employees").build();
    }

    public CompletableFuture<String> callEndpoint(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<List<String>> callAllEndpoints(List<String> endpoints) {
        List<CompletableFuture<String>> futures = endpoints.stream()
                .map(this::callEndpoint)
                .collect(Collectors.toList());

        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }
    
   
}
