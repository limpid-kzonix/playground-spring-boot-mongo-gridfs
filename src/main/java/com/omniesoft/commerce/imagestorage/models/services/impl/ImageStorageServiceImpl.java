package com.omniesoft.commerce.imagestorage.models.services.impl;

import com.mongodb.DB;
import com.omniesoft.commerce.common.handler.exception.custom.UsefulException;
import com.omniesoft.commerce.common.handler.exception.custom.enums.ImageModuleErrorCodes;
import com.omniesoft.commerce.imagestorage.models.dto.ImageDto;
import com.omniesoft.commerce.imagestorage.models.repositories.PicturesRepository;
import com.omniesoft.commerce.imagestorage.models.services.ImageOperationsService;
import com.omniesoft.commerce.imagestorage.models.services.ImageStorageService;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "pictures")
public class ImageStorageServiceImpl implements ImageStorageService {

    private PicturesRepository picturesRepository;

    private RandomStringGenerator randomStringGenerator;

    private ImageOperationsService imageOperationsService;

    private final MongoTemplate mongoTemplate;

    @Override
    public String store(MultipartFile file) {

        BufferedImage read = getBufferedImage(file);
        DB db = mongoTemplate.getDb();
        if (!db.command("{ getLastError: 1 }").ok()) {
            throw new UsefulException();
        }
        String generated = randomStringGenerator.generate(40);
        try {
            prepareAndSave(file, read, generated).get();
            return generated;
        } catch (Exception e) {
            throw new UsefulException();
        }


    }

    private BufferedImage getBufferedImage(MultipartFile file) {
        try {
            return ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new UsefulException();
        }
    }


    private CompletableFuture<Void> prepareAndSave(MultipartFile file, BufferedImage read, String generated) {

        CompletableFuture.runAsync(() -> writeOriginal(file, read, generated));
        CompletableFuture.runAsync(() -> writeLarge(file, read, generated));
        CompletableFuture.runAsync(() -> writeSmall(file, read, generated));
        CompletableFuture.runAsync(() -> writeMedium(file, read, generated));
        return CompletableFuture.completedFuture(null);
    }

    @Async(value = "mongoExecutionWritableContext")
    void writeMedium(MultipartFile file, BufferedImage read, String generated) {

        CompletableFuture.runAsync(() -> {
            try {
                picturesRepository
                        .storeSource(imageOperationsService.prepareMedium(read), generated, file
                                .getContentType(), ImageType.MEDIUM);
            } catch (IOException e) {
                throw new UsefulException(ImageModuleErrorCodes.IMAGE_PROCESSING_ERROR);
            }
        });

    }

    @Async(value = "mongoExecutionWritableContext")
    void writeSmall(MultipartFile file, BufferedImage read, String generated) {

        CompletableFuture.runAsync(() -> {
            try {
                picturesRepository
                        .storeSource(imageOperationsService.prepareSmall(read), generated, file
                                .getContentType(), ImageType.SMALL);
            } catch (IOException e) {
                throw new UsefulException(ImageModuleErrorCodes.IMAGE_PROCESSING_ERROR);
            }
        });

    }

    @Async(value = "mongoExecutionWritableContext")
    void writeLarge(MultipartFile file, BufferedImage read, String generated) {

        CompletableFuture.runAsync(() -> {
            try {
                picturesRepository
                        .storeSource(imageOperationsService.prepareLarge(read), generated, file
                                .getContentType(), ImageType.LARGE);
            } catch (IOException e) {
                throw new UsefulException(ImageModuleErrorCodes.IMAGE_PROCESSING_ERROR);
            }
        });

    }

    @Async(value = "mongoExecutionWritableContext")
    void writeOriginal(MultipartFile file, BufferedImage read, String generated) {

        CompletableFuture.runAsync(() -> {
            try {
                picturesRepository
                        .storeSource(imageOperationsService.prepareOriginal(read), generated, file
                                .getContentType(), ImageType.ORIGINAL);
            } catch (IOException e) {
                throw new UsefulException(ImageModuleErrorCodes.IMAGE_PROCESSING_ERROR);
            }
        });

    }

    @Override
    @Cacheable(value="pictures", key = "{ #root.methodName, #imageId, #type }")
    public ImageDto fetchImageByIdAndType(String imageId, ImageType type) {
        log.info(" Loading images with image-id {} and image-type {}", imageId, type);
        ImageDto imageDto = picturesRepository.fetchPicturesSource(imageId, type);
        return imageDto;
    }

    @Async("mongoExecutionWritableContext")
    @Override
    public void delete(String imageId) {

        picturesRepository.deleteImage(imageId);
    }
}
