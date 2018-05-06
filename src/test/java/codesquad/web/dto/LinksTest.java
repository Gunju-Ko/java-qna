package codesquad.web.dto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class LinksTest {

    private String url;

    private Page<?> page;

    @Before
    public void setUp() throws Exception {
        url = "/api/questions";
        page = mock(Page.class);
    }

    @Test
    public void 다음페이지_이전페이지가_존재() throws Exception {
        given(page.hasPrevious()).willReturn(true);
        given(page.hasNext()).willReturn(true);
        given(page.nextPageable()).willReturn(nextPageable());
        given(page.previousPageable()).willReturn(prevPageable());

        Links links = Links.builder()
                           .page(page)
                           .url(url)
                           .build();
        assertThat(links.getNext()).isNotNull();
        assertThat(links.getPrev()).isNotNull();
        assertThat(links.getNext().getRel()).isEqualTo(Link.NEXT);
        assertThat(links.getPrev().getRel()).isEqualTo(Link.PREV);
        assertThat(links.getNext().getHref()).isEqualTo("/api/questions?page=2&size=10");
        assertThat(links.getPrev().getHref()).isEqualTo("/api/questions?page=0&size=10");
    }

    @Test
    public void 다음페이지만_존재() throws Exception {
        given(page.hasPrevious()).willReturn(false);
        given(page.hasNext()).willReturn(true);
        given(page.nextPageable()).willReturn(nextPageable());

        Links links = Links.builder()
                           .page(page)
                           .url(url)
                           .build();

        assertThat(links.getNext()).isNotNull();
        assertThat(links.getPrev()).isNull();
        assertThat(links.getNext().getRel()).isEqualTo(Link.NEXT);
        assertThat(links.getNext().getHref()).isEqualTo("/api/questions?page=2&size=10");
    }

    @Test
    public void 이전페이지만_존재() throws Exception {
        given(page.hasPrevious()).willReturn(true);
        given(page.hasNext()).willReturn(false);
        given(page.previousPageable()).willReturn(prevPageable());

        Links links = Links.builder()
                           .page(page)
                           .url(url)
                           .build();

        assertThat(links.getNext()).isNull();
        assertThat(links.getPrev()).isNotNull();
        assertThat(links.getPrev().getRel()).isEqualTo(Link.PREV);
        assertThat(links.getPrev().getHref()).isEqualTo("/api/questions?page=0&size=10");
    }

    private Pageable nextPageable() throws Exception {
        return new PageRequest(2, 10);

    }

    private Pageable prevPageable() throws Exception {
        return new PageRequest(0, 10);
    }

}