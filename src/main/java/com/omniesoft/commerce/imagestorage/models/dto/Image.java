package com.omniesoft.commerce.imagestorage.models.dto;

import com.google.common.base.Objects;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String pictureIdentifier;
    private ImageType imageType;
    private InputStream stream;
    private String contentType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        if (!super.equals(o)) return false;
        Image image = (Image) o;
        return Objects.equal(getPictureIdentifier(), image.getPictureIdentifier()) &&
                getImageType() == image.getImageType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getPictureIdentifier(), getImageType());
    }
}
