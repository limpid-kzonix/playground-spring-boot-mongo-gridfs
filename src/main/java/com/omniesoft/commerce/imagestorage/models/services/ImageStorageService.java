package com.omniesoft.commerce.imagestorage.models.services;

import com.omniesoft.commerce.imagestorage.models.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String store(MultipartFile file);

    ImageDto fetchImageByIdAndType(String imageId, ImageType type);

    void delete(String imageId);
}
