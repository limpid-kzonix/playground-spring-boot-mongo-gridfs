package com.omniesoft.commerce.imagestorage.models.repositories.impl;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.omniesoft.commerce.common.handler.exception.custom.UsefulException;
import com.omniesoft.commerce.common.handler.exception.custom.enums.InternalErrorCodes;
import com.omniesoft.commerce.imagestorage.models.dto.Image;
import com.omniesoft.commerce.imagestorage.models.repositories.PicturesRepository;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component
public class PicturesRepositoryImpl implements PicturesRepository {

    private GridFsTemplate gridFsTemplate;


    @Override
    public Image fetchPicturesSource(String picturesIdentifier, ImageType type) {

        List<GridFSDBFile> files = gridFsTemplate
                .find(new Query(Criteria.where("filename").is(picturesIdentifier).and("metadata.imageSizeType")
                        .is(type.name())));
        if (files.isEmpty()) {
            throw new UsefulException("Image with identifier: " + picturesIdentifier, InternalErrorCodes.RESOURCE_NOT_FOUND);
        }
        GridFSDBFile gridFSDBFile = files.get(0);
        return new Image(gridFSDBFile.getInputStream(), gridFSDBFile.getContentType());
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
