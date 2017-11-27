package com.omniesoft.commerce.imagestorage;

import com.omniesoft.commerce.imagestorage.models.dto.Image;
import com.omniesoft.commerce.imagestorage.models.services.ImageStorageService;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class OmnieCommerceImageStorageApplicationTests {

	@Autowired ImageStorageService imageStorageService;


	@Test
	public void contextLoads() {
	}


	@Test
	public void getImage() {

		Image image = imageStorageService
				.fetchImageByIdAndType("4hXMGCw9Iv4jvSZzHySUUvBzT34zQzcvwboGfyUN", ImageType.MEDIUM);
		log.info(image.getContentType());
		Assert.assertEquals("image type not compatible","image/png", image.getContentType());
	}
}


