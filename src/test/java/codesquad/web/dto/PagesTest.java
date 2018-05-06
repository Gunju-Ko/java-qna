package codesquad.web.dto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PagesTest {

    private Page page;

    private Pageable pageable;

    @Before
    public void setUp() throws Exception {
        page = mock(Page.class);
        pageable = mock(Pageable.class);
    }

    @Test
    public void 첫번째페이지까지존재() throws Exception {
        when(pageable.getPageNumber()).thenReturn(1);
        when(pageable.getPageSize()).thenReturn(5);

        // 첫번째 페이지만 존재
        when(page.getTotalPages()).thenReturn(1);

        Pages pages = Pages.of(page, pageable, "/");

        assertThat(pages.getPages().size()).isEqualTo(1);
        IntStream.rangeClosed(1, 1)
                 .forEach(i -> {
                     PageInfo pageInfo = pages.getPages().get(i - 1);

                     assertThat(pageInfo.getPage()).isEqualTo(i);
                     assertThat(pageInfo.isCurrent()).isTrue();
                     assertThat(pageInfo.getHref()).isEqualTo(String.format("/?page=%d", i - 1));
                 });
    }

    @Test
    public void 세번째페이지까지존재() throws Exception {
        when(pageable.getPageNumber()).thenReturn(1);
        when(pageable.getPageSize()).thenReturn(5);

        // 세번째 페이지만 존재
        when(page.getTotalPages()).thenReturn(3);

        Pages pages = Pages.of(page, pageable, "/");

        assertThat(pages.getPages().size()).isEqualTo(3);

        IntStream.rangeClosed(1, 3)
                 .forEach(i -> {
                     PageInfo pageInfo = pages.getPages().get(i - 1);

                     assertThat(pageInfo.getPage()).isEqualTo(i);
                     assertThat(pageInfo.isCurrent()).isEqualTo(i == 1);
                     assertThat(pageInfo.getHref()).isEqualTo(String.format("/?page=%d", i - 1));
                 });
    }

    @Test
    public void 일곱번째페이지까지존재() throws Exception {
        when(pageable.getPageNumber()).thenReturn(1);
        when(pageable.getPageSize()).thenReturn(5);

        // 일곱번째 페이지만 존재
        when(page.getTotalPages()).thenReturn(7);

        Pages pages = Pages.of(page, pageable, "/");

        assertThat(pages.getPages().size()).isEqualTo(5);

        IntStream.rangeClosed(1, 5)
                 .forEach(i -> {
                     PageInfo pageInfo = pages.getPages().get(i - 1);

                     assertThat(pageInfo.getPage()).isEqualTo(i);
                     assertThat(pageInfo.isCurrent()).isEqualTo(i == 1);
                     assertThat(pageInfo.getHref()).isEqualTo(String.format("/?page=%d", i - 1));
                 });
    }

    @Test
    public void 일곱번째페이지까지존재_현재세번째페이지() throws Exception {
        when(pageable.getPageNumber()).thenReturn(3);
        when(pageable.getPageSize()).thenReturn(5);

        // 일곱번째 페이지만 존재
        when(page.getTotalPages()).thenReturn(7);

        Pages pages = Pages.of(page, pageable, "/");

        assertThat(pages.getPages().size()).isEqualTo(5);

        IntStream.rangeClosed(1, 5)
                 .forEach(i -> {
                     PageInfo pageInfo = pages.getPages().get(i - 1);

                     assertThat(pageInfo.getPage()).isEqualTo(i);
                     assertThat(pageInfo.isCurrent()).isEqualTo(i == 3);
                     assertThat(pageInfo.getHref()).isEqualTo(String.format("/?page=%d", i - 1));
                 });
    }
}