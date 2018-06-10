package codesquad.web.api;

import codesquad.web.dto.Link;
import codesquad.web.dto.QuestionDto;
import codesquad.web.dto.QuestionsDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    private QuestionDto question;

    private QuestionDto updateQuestion;

    @Before
    public void setUp() throws Exception {
        question = QuestionDto.builder()
                              .title("title")
                              .contents("contents")
                              .build();

        updateQuestion = QuestionDto.builder()
                                    .title("update title")
                                    .contents("update contents")
                                    .build();
    }

    @Test
    public void showPage_첫번째페이지() throws Exception {
        ResponseEntity<QuestionsDto> response = template().getForEntity("/api/questions", QuestionsDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        QuestionsDto body = response.getBody();
        assertThat(body.getSize()).isEqualTo(3);
        assertThat(body.getPrevLink()).isNull();
        assertThat(body.getNextLink()).isNull();

    }

    @Test
    public void showPage_두번째페이지() throws Exception {
        ResponseEntity<QuestionsDto> response = template().getForEntity("/api/questions?page=2", QuestionsDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        QuestionsDto body = response.getBody();
        assertThat(body.getSize()).isEqualTo(0);
        assertThat(body.getPrevLink()).isNotNull();
        assertThat(body.getPrevLink().getHref()).isEqualTo("/api/questions?page=1&size=10");
        assertThat(body.getPrevLink().getRel()).isEqualTo(Link.PREV);

        assertThat(body.getNextLink()).isNull();
    }

    @Test
    public void show() {
        ResponseEntity<QuestionDto> response = template().getForEntity("/api/questions/1", QuestionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void show_존재하지않는Question() {
        ResponseEntity<ErrorResponse> response = template().getForEntity("/api/questions/10", ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void create() {
        // when
        String location = createResource("/api/questions", question, defaultUser());

        // then
        QuestionDto dbQuestion = getResource(location, QuestionDto.class);
        assertThat(compareTitleAndContents(dbQuestion, question)).isTrue();

        deleteResource(location, defaultUser());
    }

    @Test
    public void create_Question형식이잘못된경우() {
        // 제목이 너무 짧은 경우
        question.setTitle("t");

        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/api/questions", question, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void update() {
        // given
        String location = createResource("/api/questions", question, defaultUser());

        // when
        basicAuthTemplate(defaultUser()).put(location, updateQuestion);

        // then
        QuestionDto dbQuestion = getResource(location, QuestionDto.class);
        assertThat(compareTitleAndContents(dbQuestion, updateQuestion)).isTrue();

        deleteResource(location, defaultUser());
    }

    @Test
    public void update_권한이없는사용자() {
        // given
        String location = createResource("/api/questions", question, defaultUser());

        // when - 권한이 없는 사용자인 경우
        basicAuthTemplate(findByUserId("sanjigi")).put(location, updateQuestion);

        // then
        QuestionDto dbQuestion = getResource(location, QuestionDto.class);
        assertThat(compareTitleAndContents(dbQuestion, updateQuestion)).isFalse();

        deleteResource(location, defaultUser());
    }

    @Test
    public void delete() {
        String location = createResource("/api/questions", question, defaultUser());
        deleteResource(location, defaultUser());

        ResponseEntity<ErrorResponse> response = template().getForEntity(location, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void delete_권한이없는사용자() {
        String location = createResource("/api/questions", question, defaultUser());
        deleteResource(location, findByUserId("sanjigi"));

        ResponseEntity<QuestionDto> response = template().getForEntity(location, QuestionDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        deleteResource(location, defaultUser());
    }

    private boolean compareTitleAndContents(QuestionDto q1, QuestionDto q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException();
        }
        return Objects.equals(q1.getTitle(), q2.getTitle()) && Objects.equals(q1.getContents(), q2.getContents());
    }
}
