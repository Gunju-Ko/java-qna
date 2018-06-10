package codesquad.domain;

import support.domain.ApiUrlGeneratable;

import javax.persistence.Embeddable;
import java.net.URI;
import java.util.Objects;

@Embeddable
public class Photo implements ApiUrlGeneratable {

    private String location;

    public Photo() {
    }

    private Photo(String location) {
        this.location = Objects.requireNonNull(location);
    }

    public String getLocation() {
        return location;
    }

    public static Photo of(String location) {
        return new Photo(location);
    }

    @Override
    public URI generateApiUri() {
        return URI.create(String.format("%s", getLocation()));
    }
}
