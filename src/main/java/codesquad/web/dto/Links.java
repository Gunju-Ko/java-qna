package codesquad.web.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
public class Links {

    private Link prev;

    private Link next;

    public Links(String url, Page<?> page) {
        this.prev = makePrev(page, url);
        this.next = makeNext(page, url);
    }

    public Links() {
    }

    public static LinksBuilder builder() {
        return new LinksBuilder();
    }

    private Link makeNext(Page<?> page, String url) {
        if (!page.hasNext()) {
            return null;
        }
        return new Link(Link.NEXT, makeHref(url, page.nextPageable()));
    }

    private Link makePrev(Page<?> page, String url) {
        if (!page.hasPrevious()) {
            return null;
        }
        return new Link(Link.PREV, makeHref(url, page.previousPageable()));
    }

    private String makeHref(String url, Pageable pageable) {
        assert url != null;
        assert pageable != null;
        return String.format("%s?page=%d&size=%d", url, pageable.getPageNumber(), pageable.getPageSize());
    }

    public static class LinksBuilder {
        private String url;

        private Page<?> page;

        public LinksBuilder page(Page<?> pageable) {
            this.page = pageable;
            return this;
        }

        public LinksBuilder url(String url) {
            this.url = url;
            return this;
        }

        public Links build() {
            assert url != null;
            assert page != null;

            return new Links(this.url, this.page);
        }
    }

}
