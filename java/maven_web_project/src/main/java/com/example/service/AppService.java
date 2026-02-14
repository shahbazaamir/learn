package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.example.constants.Constants.LINES;

@Service
public class AppService implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AppService.class);

    @Value("${server.port:8080}")
    private int port;

    @PostConstruct
    public void postConstruct() {
        logger.info("{}   Post construct , prop : {}",LINES,port);
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("{} Perform operations after props are set  , prop : {} ",LINES,port);
    }

    @PreDestroy
    public void preDestroy() {
        logger.info("{} Perform operations before bean destruction ",LINES);
    }

    @Override
    public void destroy() {
        logger.info("{} Perform operations on bean destruction ",LINES);
    }
}
