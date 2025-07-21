package com.example.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestClient;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    public AdminController(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
        this.discoveryClient = discoveryClient;
        restClient = restClientBuilder.build();
    }

    @GetMapping("inventory/assets")
    public ResponseEntity<Object> view(String employeeId) {
        ServiceInstance serviceInstance = discoveryClient.getInstances("inventory-service").get(0);
        String serviceAResponse = restClient.get()
                .uri(serviceInstance.getUri() + "/api/v2/inventory/assets")
                .retrieve()
                .body(String.class);
        return ResponseEntity.status(200).body(serviceAResponse );
    }

}
