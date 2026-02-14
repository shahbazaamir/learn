package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    public void customInit() {
        logger.info("DataService initialized via @Bean initMethod");
    }

    public void customDestroy() {
        logger.info("DataService destroyed via @Bean destroyMethod");
    }
}