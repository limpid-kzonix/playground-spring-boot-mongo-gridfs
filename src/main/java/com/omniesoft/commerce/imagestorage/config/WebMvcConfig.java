package com.omniesoft.commerce.imagestorage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        for (HttpMessageConverter<?> converter : converters) {
            if (converter.getClass().equals(MappingJackson2HttpMessageConverter.class)) {
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper()
                        .registerModule(new Hibernate5Module());
            }
        }
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new Hibernate5Module());
        objectMapper.registerModule(new JavaTimeModule());

        converter.setObjectMapper(objectMapper);
        converter.setPrettyPrint(true);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(converter);

        super.extendMessageConverters(converters);
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
