package com.omniesoft.commerce.imagestorage.omniecommerceimagestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableMongoRepositories
@SpringBootApplication
public class OmnieCommerceImagestorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmnieCommerceImagestorageApplication.class, args);
	}
}
