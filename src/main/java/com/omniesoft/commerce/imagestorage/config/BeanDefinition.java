package com.omniesoft.commerce.imagestorage.config;

import com.omniesoft.commerce.common.handler.exception.handler.RestResponseEntityExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Configuration
public class BeanDefinition {
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public ResponseEntityExceptionHandler entityExceptionHandler() {

        return new RestResponseEntityExceptionHandler();
    }


}
