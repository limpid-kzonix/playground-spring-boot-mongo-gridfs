package com.omniesoft.commerce.imagestorage.models.repositories.impl;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.omniesoft.commerce.common.handler.exception.custom.UsefulException;
import com.omniesoft.commerce.common.handler.exception.custom.enums.ImageModuleErrorCodes;
import com.omniesoft.commerce.common.handler.exception.custom.enums.InternalErrorCodes;
import com.omniesoft.commerce.imagestorage.models.dto.ImageDto;
import com.omniesoft.commerce.imagestorage.models.repositories.PicturesRepository;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component
public class PicturesRepositoryImpl implements PicturesRepository {

    private GridFsTemplate gridFsTemplate;


    @Override
    public ImageDto fetchPicturesSource(String picturesIdentifier, ImageType type) {

        GridFSDBFile file = getGridFSDBFile(picturesIdentifier, type);
        if (file == null && file.getInputStream() == null) {
            throw new UsefulException("ImageDto with identifier: " + picturesIdentifier, InternalErrorCodes.RESOURCE_NOT_FOUND);
        }

        return getImageDto(picturesIdentifier, type, file);


    }

    private GridFSDBFile getGridFSDBFile(String picturesIdentifier, ImageType type) {
        return gridFsTemplate
                .findOne(new Query(Criteria.where("filename").is(picturesIdentifier).and("metadata.imageSizeType")
                        .is(type.name())));
    }

    private ImageDto getImageDto(String picturesIdentifier, ImageType type, GridFSDBFile file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            file.writeTo(baos);
            byte[] bytes = baos.toByteArray();
            return new ImageDto(picturesIdentifier, type, bytes, file.getContentType());
        } catch (IOException e) {
            throw new UsefulException("Cant read image from storage", ImageModuleErrorCodes.CAN_NOT_FETCH_IMAGE_FROM_DB);
        }
    }

    @Override
    public String storeSource(InputStream inputStream, String originalFileName, String contentType, ImageType type)
            throws IOException {

        log.info("ORIGINAL FILE NAME (image-identifier): {}", originalFileName);


        Map<String, Object> metaData = metaData(originalFileName, type);

        GridFSFile store = gridFsTemplate
                .store(inputStream, originalFileName, contentType,
                        metaData);
        return store.getId().toString();
    }


    @Override
    public void deleteImage(String imageIdentifier) {
        gridFsTemplate.delete(new Query(Criteria.where("filename").is(imageIdentifier)));
    }

    /* ---------------------------------------------------------------------------------------------------------------*/

    private Map<String, Object> metaData(String originalFileName, ImageType type) {

        Map<String, Object> metaData = new HashMap<>();
        metaData.put("orginalFileName", originalFileName);
        metaData.put("imageSizeType", type.name());
        metaData.put("dateTime", LocalDateTime.now());
        return metaData;
    }
}
