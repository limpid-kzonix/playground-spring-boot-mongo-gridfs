package com.omniesoft.commerce.imagestorage.models.services.impl;

import com.omniesoft.commerce.imagestorage.models.dto.Image;
import com.omniesoft.commerce.imagestorage.models.services.ImageOperationsService;
import com.omniesoft.commerce.imagestorage.models.services.ImageStorageService;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import com.omniesoft.commerce.imagestorage.models.repositories.PicturesRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService {

	private PicturesRepository picturesRepository;

	private RandomStringGenerator randomStringGenerator;

	private ImageOperationsService imageOperationsService;

	@Override
	public String store(MultipartFile file) throws IOException {

		BufferedImage read = ImageIO.read(file.getInputStream());

		String generated = randomStringGenerator.generate(40);

		prepareAndSave(file, read, generated);
		return generated;

	}

	@Async(value = "mongoExecutionWritableContext")
	public CompletableFuture<Void> prepareAndSave(MultipartFile file, BufferedImage read, String generated) throws
			IOException
	{

		return CompletableFuture.runAsync(() -> {
			writeLarge(file, read, generated);
			writeSmall(file, read, generated);
			writeMedium(file, read, generated);
		});
	}

	private void writeMedium(MultipartFile file, BufferedImage read, String generated) {

		CompletableFuture.runAsync(() -> {
			try {
				picturesRepository
						.storeSource(imageOperationsService.prepareMedium(read), generated, file
								.getContentType(), ImageType.MEDIUM);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		});

	}

	private void writeSmall(MultipartFile file, BufferedImage read, String generated) {

		CompletableFuture.runAsync(() -> {
			try {
				picturesRepository
						.storeSource(imageOperationsService.prepareSmall(read), generated, file
								.getContentType(), ImageType.SMALL);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		});

	}

	private void writeLarge(MultipartFile file, BufferedImage read, String generated) {

		CompletableFuture.runAsync(() -> {
			try {
				picturesRepository
						.storeSource(imageOperationsService.prepareLarge(read), generated, file
								.getContentType(), ImageType.LARGE);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		});

	}

	@Override
	public Image fetchImageByIdAndType(String imageId, ImageType type) {

		return picturesRepository.fetchPicturesSource(imageId, type);
	}

	@Async("mongoExecutionWritableContext")
	@Override
	public void delete(String imageId) {

		picturesRepository.deleteImage(imageId);
	}


}
