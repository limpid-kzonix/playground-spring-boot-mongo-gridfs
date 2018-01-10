package com.omniesoft.commerce.imagestorage.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private InputStream stream;
    private String contentType;


}
