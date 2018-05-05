package codesquad.web.dto;

import lombok.Data;

@Data
public class PageInfo {

    private final int page;

    private final boolean current;

    private final String href;

    public PageInfo(String url, int page, boolean current) {
        this.page = page;
        this.current = current;
        this.href = String.format("%s?page=%d", url, page - 1);
    }
}
