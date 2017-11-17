package com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.models.services;

import com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.models.dto.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {

	String store(MultipartFile file) throws IOException;

	Image fetchImageByIdAndType(String imageId, ImageType type);

	void delete(String imageId);
}
