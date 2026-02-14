package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static com.example.constants.Constants.LINES;

@Service
public class CommonService {

    private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
    
    @Value("${server.port:8080}")
    private int port;

    @EventListener
    public void onApplicationStarting(ApplicationStartingEvent event) {
        logger.info("{} Application starting",LINES);
    }

    @EventListener
    public void onApplicationEnvironmentPrepared(ApplicationEnvironmentPreparedEvent event) {
        logger.info("{} Application environment prepared",LINES);
    }

    @EventListener
    public void onApplicationContextInitialized(ApplicationContextInitializedEvent event) {
        logger.info("{} Application context initialized",LINES);
    }

    @EventListener
    public void onApplicationPrepared(ApplicationPreparedEvent event) {
        logger.info("{} Application prepared",LINES);
    }

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        logger.info("{} Context refreshed",LINES);
    }

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        logger.info("{} Application started",LINES);
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        logger.info("{} Application ready on port {}", LINES, port);
    }
}
