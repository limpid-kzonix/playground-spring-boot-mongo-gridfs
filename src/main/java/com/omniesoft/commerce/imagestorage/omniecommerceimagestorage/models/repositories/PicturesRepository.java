package com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.models.repositories;

import com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.models.dto.Image;
import com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.models.services.ImageType;

import java.io.IOException;
import java.io.InputStream;

public interface PicturesRepository {

	Image fetchPicturesSource(String picturesIdentifier, ImageType type);

	String storeSource(InputStream inputStream, String originalFileName, String contentType, ImageType type) throws IOException;

	void deleteImage(String imageIdentifier);

}
