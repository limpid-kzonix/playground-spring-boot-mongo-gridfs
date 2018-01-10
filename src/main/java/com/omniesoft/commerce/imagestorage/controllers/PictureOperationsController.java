package com.omniesoft.commerce.imagestorage.controllers;

import com.omniesoft.commerce.imagestorage.models.dto.Image;
import com.omniesoft.commerce.imagestorage.models.services.ImageStorageService;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class PictureOperationsController {

    private ImageStorageService service;

    @PostMapping(path = "/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadImage(@RequestParam("file") MultipartFile file) {

        try {
            return service.store(file);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @DeleteMapping(path = "/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@RequestParam("image-identifier") String imageId) {

        service.delete(imageId);

    }

    @GetMapping(path = "/fetch")
    public ResponseEntity<InputStreamResource> fetchImage(@RequestParam("image-identifier") String imageId,
                                                          @RequestParam("image-type") ImageType type) {

        Image image = service.fetchImageByIdAndType(imageId, type);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, image.getContentType());

        return new ResponseEntity<InputStreamResource>(new InputStreamResource(image.getStream()), headers,
                HttpStatus.OK);
    }

}
