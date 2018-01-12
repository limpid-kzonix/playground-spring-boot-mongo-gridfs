package com.omniesoft.commerce.imagestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableMongoRepositories
@SpringBootApplication
@EnableCaching
public class OmnieCommerceImageStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmnieCommerceImageStorageApplication.class, args);
    }
}
