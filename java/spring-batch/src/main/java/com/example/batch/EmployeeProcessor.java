package com.example.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, EmployeeStatus> {
    
    @Autowired
    private WebClient webClient;
    
    @Override
    public EmployeeStatus process(Employee employee) throws Exception {
        EmployeeStatus status = new EmployeeStatus(employee);
        
        try {
            ApiResponse response = callApiWithRetry(employee);
            
            if (response != null && !response.getName().isEmpty()) {
                status.setApiStatus("SUCCESS");
                System.out.println("API called for employee: " + employee.getFirstName() + 
                                 ", API response: " + response.getName());
            } else {
                status.setApiStatus("FAILED");
                status.setErrorMessage("Empty API response");
            }
            
        } catch (Exception e) {
            status.setApiStatus("FAILED");
            status.setErrorMessage(e.getMessage());
            System.err.println("API call failed for employee: " + employee.getFirstName() + ", Error: " + e.getMessage());
        }
        
        return status;
    }
    
    private ApiResponse callApiWithRetry(Employee employee) {
        try {
            return webClient.get()
                    .uri("/users/1")
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))
                        .filter(this::shouldRetryWithoutBackoff)
                    )
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(this::shouldRetryWithBackoff)
                    )
                    .block();
        } catch (Exception e) {
            throw e;
        }
    }
    
    private boolean shouldRetryWithBackoff(Throwable throwable) {
        if (throwable instanceof WebClientResponseException ex) {
            int status = ex.getStatusCode().value();
            return status == 401 || status == 502 || status == 503;
        }
        return false;
    }
    
    private boolean shouldRetryWithoutBackoff(Throwable throwable) {
        if (throwable instanceof WebClientResponseException ex) {
            int status = ex.getStatusCode().value();
            return status == 500;
        }
        return false;
    }
    
    // Simple DTO for API response
    public static class ApiResponse {
        private String name = "";
        private String email = "";
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}