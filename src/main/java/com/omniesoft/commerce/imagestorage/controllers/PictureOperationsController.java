package com.omniesoft.commerce.imagestorage.controllers;

import com.omniesoft.commerce.common.responce.ResponseMessage;
import com.omniesoft.commerce.imagestorage.models.dto.ImageDto;
import com.omniesoft.commerce.imagestorage.models.services.ImageStorageService;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class PictureOperationsController {

    private ImageStorageService service;

    @PostMapping(path = "/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage uploadImage(@RequestParam("file") MultipartFile file) {
        return new ResponseMessage(service.store(file));
    }

    @DeleteMapping(path = "/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@RequestParam("image-identifier") String imageId) {

        service.delete(imageId);

    }

    @GetMapping(path = "/fetch")
    public ResponseEntity<byte[]> fetchImage(@RequestParam("image_identifier") String imageId,
                                             @RequestParam("image_type") ImageType type) {

        ImageDto imageDto = service.fetchImageByIdAndType(imageId, type);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, imageDto.getContentType());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageDto.getContentType()))
                .headers(headers)
                .contentLength(imageDto.getStream().length)
                .body(imageDto.getStream());
    }


}
