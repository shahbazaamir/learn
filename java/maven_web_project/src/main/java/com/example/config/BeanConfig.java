package com.example.config;

import com.example.service.DataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public DataService dataService() {
        return new DataService();
    }
}