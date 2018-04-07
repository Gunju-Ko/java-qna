package codesquad.domain;

import support.domain.ApiUrlGeneratable;

import javax.persistence.Embeddable;
import java.net.URI;
import java.util.Objects;

@Embeddable
public class Photo implements ApiUrlGeneratable {

    private String filePath;

    public Photo(String filePath) {
        this.filePath = Objects.requireNonNull(filePath);
    }

    public Photo() {
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public URI generateApiUri() {
        return URI.create(String.format("%s", getFilePath()));
    }
}
