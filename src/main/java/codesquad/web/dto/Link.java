package codesquad.web.dto;

import lombok.Data;

@Data
public class Link {

    public static final String NEXT = "next";

    public static final String PREV = "prev";

    private String rel;

    private String href;

    public Link(String rel, String href) {
        this.rel = rel;
        this.href = href;
    }

    public Link() {
    }
}
