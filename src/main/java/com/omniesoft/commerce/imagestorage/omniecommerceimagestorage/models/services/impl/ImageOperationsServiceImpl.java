package com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.models.services.impl;

import com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.models.services.ImageOperationsService;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageOperationsServiceImpl implements ImageOperationsService {

	private BufferedImage crop(BufferedImage bufferedImage, Rectangle2D property) {

		return Scalr.crop(bufferedImage, (int) property.getX(), (int) property.getY(), (int) property.getWidth(),
				(int) property.getHeight());
	}

	private Rectangle2D getImageDimension(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();

		int hCenter = width / 2;
		int vCenter = height / 2;

		int dimension = image.getWidth() > image.getHeight
				() ? image.getHeight() : image.getWidth();

		return new Rectangle(hCenter - dimension / 2, vCenter - dimension / 2, dimension, dimension);
	}

	private InputStream transform(BufferedImage image) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

	@Override
	public InputStream prepareSmall(BufferedImage originalImage) throws IOException
	{

		return transform(
				Scalr.resize(crop(originalImage, getImageDimension(originalImage)), Scalr.Method.QUALITY,
						Scalr.Mode.FIT_EXACT, 150, 150));
	}

	@Override
	public InputStream prepareMedium(BufferedImage originalImage) throws IOException
	{

		return transform(Scalr.resize(crop(originalImage, getImageDimension(originalImage)), Scalr.Method.QUALITY,
				Scalr.Mode.FIT_EXACT, 300, 300));
	}

	@Override
	public InputStream prepareLarge(BufferedImage originalImage) throws IOException
	{

		return transform(
				Scalr.resize(crop(originalImage, getImageDimension(originalImage)), Scalr.Method.SPEED,
						Scalr.Mode.FIT_EXACT, 500, 500));
	}

}
