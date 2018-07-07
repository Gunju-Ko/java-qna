package codesquad.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Paged<T> {

    private List<T> contents;

    private Pages pages;

    private Links links;

    public Paged(List<T> contents, Pages pages, Links links) {
        this.contents = contents;
        this.pages = pages;
        this.links = links;
    }

    public static <R> Paged<R> of(Page<R> page, Pageable pageable, String url) {
        assert page != null;
        assert pageable != null;
        assert url != null;
        return new Paged<>(makeContents(page), makePages(page, pageable, url), makeLinks(page, url));
    }

    private static <R> Pages makePages(Page<R> page, Pageable pageable, String url) {
        return Pages.of(page, pageable, url);
    }

    private static <R> List<R> makeContents(Page<R> page) {
        return page.getContent();
    }

    private static <R> Links makeLinks(Page<R> page, String url) {
        return Links.builder()
                    .url(url)
                    .page(page)
                    .build();
    }

    public <R> Paged<R> map(Function<T, R> function) {
        List<R> contents = this.contents.stream()
                                        .map(function)
                                        .collect(Collectors.toList());
        return new Paged<>(contents, this.pages, this.links);
    }

    @JsonIgnore
    public Link getPrevLink() {
        return links.getPrev();
    }

    @JsonIgnore
    public Link getNextLink() {
        return links.getNext();
    }

    @JsonIgnore
    public boolean isHasNext() {
        return links.getNext() != null;
    }

    @JsonIgnore
    public boolean isHasPrev() {
        return links.getPrev() != null;
    }

    @JsonIgnore
    public int getSize() {
        return contents.size();
    }
}