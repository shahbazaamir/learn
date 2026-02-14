package com.example.graphql.service;

import com.example.graphql.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    public List<User> getAllUsers() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();
    }

    public User getUserById(int id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }
}
