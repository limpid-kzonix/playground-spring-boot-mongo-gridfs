package com.omniesoft.commerce.imagestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableMongoRepositories
@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableEurekaClient
public class OmnieCommerceImageStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmnieCommerceImageStorageApplication.class, args);
    }
}
