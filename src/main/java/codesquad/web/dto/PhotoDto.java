package codesquad.web.dto;

import codesquad.domain.Photo;
import lombok.Data;

@Data
public class PhotoDto {

    private String location;

    public PhotoDto(String location) {
        this.location = location;
    }

    public PhotoDto() {
    }

    public static PhotoDto of(Photo photo) {
        if (photo == null) {
            return null;
        }
        return new PhotoDto(photo.getLocation());
    }

    public Photo toPhoto() {
        return new Photo(location);
    }
}
