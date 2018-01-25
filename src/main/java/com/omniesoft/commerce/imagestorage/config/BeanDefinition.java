package com.omniesoft.commerce.imagestorage.config;

import com.omniesoft.commerce.imagestorage.config.exception.handler.ImageExtendedErrorHandler;
import lombok.AllArgsConstructor;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.Executor;

@Configuration
@AllArgsConstructor
public class BeanDefinition {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public ResponseEntityExceptionHandler entityExceptionHandler() {

        return new ImageExtendedErrorHandler();
    }

    @Bean
    RandomStringGenerator randomStringGenerator() {
        return new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
    }

    @Bean(name = "mongoExecutionWritableContext")
    public Executor httpThreadPoolExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(16 * 2);
        executor.setQueueCapacity(750);
        executor.setThreadNamePrefix("MongoWriter-ThreadPool-");
        executor.initialize();
        return executor;
    }

}
