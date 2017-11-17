package com.omniesoft.commerce.imagestorage.models.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public interface ImageOperationsService {

	InputStream prepareSmall(BufferedImage originalImage) throws IOException;

	InputStream prepareMedium(BufferedImage originalImage) throws IOException;

	InputStream prepareLarge(BufferedImage originalImage) throws IOException;


}
