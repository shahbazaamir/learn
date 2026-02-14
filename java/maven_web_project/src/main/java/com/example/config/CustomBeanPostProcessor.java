package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import static com.example.constants.Constants.LINES;

//@Component
public class CustomBeanPostProcessor {//implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CustomBeanPostProcessor.class);

   // @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.info("{} Before initialization: {}", LINES,beanName);
        return bean;
    }

    //@Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("{} After initialization: {}", LINES,beanName);
        return bean;
    }
}