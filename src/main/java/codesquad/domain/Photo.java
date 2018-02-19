package codesquad.domain;

import support.domain.ApiUrlGeneratable;

import javax.persistence.Embeddable;
import java.net.URI;

@Embeddable
public class Photo implements ApiUrlGeneratable {

    private String filePath;

    public Photo(String filePath) {
        this.filePath = filePath;
    }

    public Photo() {
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public URI generateApiUri() {
        return URI.create(String.format("/files/{%s}", getFilePath()));
    }
}
