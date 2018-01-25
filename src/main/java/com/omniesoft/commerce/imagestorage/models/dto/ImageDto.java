package com.omniesoft.commerce.imagestorage.models.dto;

import com.google.common.base.Objects;
import com.omniesoft.commerce.imagestorage.models.services.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private String pictureIdentifier;
    private ImageType imageType;
    private byte[] stream;
    private String contentType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageDto)) return false;
        if (!super.equals(o)) return false;
        ImageDto imageDto = (ImageDto) o;
        return Objects.equal(getPictureIdentifier(), imageDto.getPictureIdentifier()) &&
                getImageType() == imageDto.getImageType();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getPictureIdentifier(), getImageType());
    }
}
