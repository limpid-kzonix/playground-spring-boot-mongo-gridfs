package com.omniesoft.commerce.imagestorage;

import com.omniesoft.commerce.imagestorage.models.dto.ImageDto;
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

    @Autowired
    ImageStorageService imageStorageService;


    @Test
    public void contextLoads() {
    }


    @Test
    public void getImage() {

        ImageDto imageDto = imageStorageService
                .fetchImageByIdAndType("4hXMGCw9Iv4jvSZzHySUUvBzT34zQzcvwboGfyUN", ImageType.MEDIUM);
        log.info(imageDto.getContentType());
        Assert.assertEquals("imageDto type not compatible", "imageDto/png", imageDto.getContentType());
    }
}


