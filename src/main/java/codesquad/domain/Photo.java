package codesquad.domain;

import support.domain.ApiUrlGeneratable;

import javax.persistence.Embeddable;
import java.net.URI;
import java.util.Objects;

@Embeddable
public class Photo implements ApiUrlGeneratable {

    private String location;

    public Photo(String location) {
        this.location = Objects.requireNonNull(location);
    }

    public Photo() {
    }

    public String getLocation() {
        return location;
    }

    @Override
    public URI generateApiUri() {
        return URI.create(String.format("%s", getLocation()));
    }
}
