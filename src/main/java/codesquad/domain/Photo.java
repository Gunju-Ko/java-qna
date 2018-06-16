package codesquad.domain;

import codesquad.web.dto.PhotoDto;
import support.domain.ApiUrlGeneratable;

import javax.persistence.Embeddable;
import java.net.URI;
import java.util.Objects;

@Embeddable
public class Photo implements ApiUrlGeneratable {

    private String location;

    public Photo() {
    }

    public Photo(String location) {
        this.location = Objects.requireNonNull(location);
    }

    public static Photo of(String location) {
        return new Photo(location);
    }

    public static Photo of(PhotoDto photo) {
        if (photo == null) {
            return null;
        }
        return new Photo(photo.getLocation());
    }

    public String getLocation() {
        return location;
    }

    @Override
    public URI generateApiUri() {
        return URI.create(String.format("%s", getLocation()));
    }
}
