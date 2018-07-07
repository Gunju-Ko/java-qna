package codesquad.web.dto;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class PagedTest {

    private List<Post> contents;

    @Before
    public void setUp() throws Exception {
        contents = Arrays.asList(new Post("Java", "Java Programming is ..."),
                                 new Post("Spring", "Spring is framework ..."),
                                 new Post("Jpa", "Jpa is ..."));
    }

    @Test
    public void of_첫번째페이지만존재() throws Exception {
        Page page = Mockito.mock(Page.class);
        Pageable pageable = Mockito.mock(Pageable.class);

        // 첫번째 페이지만 존재
        when(pageable.getPageNumber()).thenReturn(1);
        when(pageable.getPageSize()).thenReturn(5);
        when(page.getTotalPages()).thenReturn(1);

        // 이전 페이지, 다음 페이지가 존재하지 않음
        doReturn(contents).when(page).getContent();
        doReturn(false).when(page).hasNext();
        doReturn(false).when(page).hasPrevious();

        Paged<Post> posts = Paged.of(page, pageable, "/posts");

        assertThat(posts.getContents()).isEqualTo(contents);
        // 이전 페이지, 다음 페이지 링크 없음 (null)
        assertThat(posts.getNextLink()).isNull();
        assertThat(posts.getPrevLink()).isNull();

        // 한개 페이지만 존재
        assertThat(posts.getPages().getPageInfos().size()).isEqualTo(1);
    }

    @Data
    private static final class Post {
        private String title;
        private String content;

        public Post(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

}