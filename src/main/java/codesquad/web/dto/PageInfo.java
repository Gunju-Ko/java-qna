package codesquad.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageInfo {

    private int pageNumber;

    private boolean current;

    private String href;

    public PageInfo(String url, int pageNumber, boolean current) {
        this.pageNumber = pageNumber;
        this.current = current;
        this.href = String.format("%s?page=%d", url, pageNumber - 1);
    }
}
