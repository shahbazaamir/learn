package com.example.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import com.example.test.service.TestService;

@RestController
@RequestMapping("/api/test/employees")
public class TestApi {

    @Autowired
    private TestService multiEndpointService;

    @GetMapping("/all")
    public List<String> fetchAll() throws ExecutionException, InterruptedException {
        List<String> endpoints = List.of(
            "/Rahul",
            "/Sachin",
            "/Saurav"
        );

        return multiEndpointService.callAllEndpoints(endpoints).get();
    }
}
