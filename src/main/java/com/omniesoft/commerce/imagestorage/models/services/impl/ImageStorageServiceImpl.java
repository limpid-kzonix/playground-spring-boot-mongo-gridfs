package com.omniesoft.commerce.imagestorage.models.services.impl;

import com.omniesoft.commerce.common.handler.exception.custom.UsefulException;
import com.omniesoft.commerce.common.handler.exception.custom.enums.InternalErrorCodes;
import com.omniesoft.commerce.imagestorage.models.dto.Image;
import com.omniesoft.commerce.imagestorage.models.repositories.PicturesRepository;
import com.omniesoft.commerce.imagestorage.models.services.ImageOperationsService;
import com.omniesoft.commerce.imagestorage.models.services.ImageStorageService;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
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
	public String store( MultipartFile file ) throws IOException {
		
		BufferedImage read = ImageIO.read( file.getInputStream( ) );
		
		String generated = randomStringGenerator.generate( 40 );
		
		prepareAndSave( file, read, generated );
		return generated;
		
	}
	
	
	private CompletableFuture<Void> prepareAndSave( MultipartFile file, BufferedImage read, String generated )
	{
		
		CompletableFuture.runAsync( () -> writeOriginal( file, read, generated ) );
		CompletableFuture.runAsync( () -> writeLarge( file, read, generated ) );
		CompletableFuture.runAsync( () -> writeSmall( file, read, generated ) );
		CompletableFuture.runAsync( () -> writeMedium( file, read, generated ) );
		return CompletableFuture.completedFuture( null );
	}
	
	@Async(value = "mongoExecutionWritableContext")
	void writeMedium( MultipartFile file, BufferedImage read, String generated ) {
		
		CompletableFuture.runAsync( () -> {
			try {
				picturesRepository
						.storeSource( imageOperationsService.prepareMedium( read ), generated, file
								.getContentType( ), ImageType.MEDIUM );
			} catch (IOException e) {
				throw new UsefulException( InternalErrorCodes.IMAGE_PROCESSING_ERROR );
			}
		} );
		
	}
	
	@Async(value = "mongoExecutionWritableContext")
	void writeSmall( MultipartFile file, BufferedImage read, String generated ) {
		
		CompletableFuture.runAsync( () -> {
			try {
				picturesRepository
						.storeSource( imageOperationsService.prepareSmall( read ), generated, file
								.getContentType( ), ImageType.SMALL );
			} catch (IOException e) {
				throw new UsefulException( InternalErrorCodes.IMAGE_PROCESSING_ERROR );
			}
		} );
		
	}
	
	@Async(value = "mongoExecutionWritableContext")
	void writeLarge( MultipartFile file, BufferedImage read, String generated ) {
		
		CompletableFuture.runAsync( () -> {
			try {
				picturesRepository
						.storeSource( imageOperationsService.prepareLarge( read ), generated, file
								.getContentType( ), ImageType.LARGE );
			} catch (IOException e) {
				throw new UsefulException( InternalErrorCodes.IMAGE_PROCESSING_ERROR );
			}
		} );
		
	}
	
	@Async(value = "mongoExecutionWritableContext")
	void writeOriginal( MultipartFile file, BufferedImage read, String generated ) {
		
		CompletableFuture.runAsync( () -> {
			try {
				picturesRepository
						.storeSource( imageOperationsService.prepareOriginal( read ), generated, file
								.getContentType( ), ImageType.ORIGINAL );
			} catch (IOException e) {
				throw new UsefulException( InternalErrorCodes.IMAGE_PROCESSING_ERROR );
			}
		} );
		
	}
	
	@Override
	public Image fetchImageByIdAndType( String imageId, ImageType type ) {
		
		return picturesRepository.fetchPicturesSource( imageId, type );
	}
	
	@Async("mongoExecutionWritableContext")
	@Override
	public void delete( String imageId ) {
		
		picturesRepository.deleteImage( imageId );
	}
	
	
}
