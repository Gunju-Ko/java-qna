package codesquad.web.api;

import codesquad.web.dto.QuestionDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void show() {
        ResponseEntity<QuestionDto> response = template().getForEntity("/api/questions/1", QuestionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    public void show_존재하지않는Question() {
        ResponseEntity<ErrorResponse> response = template().getForEntity("/api/questions/10", ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void create() {
        QuestionDto questionDto = new QuestionDto("title", "content");

        String location = createResource("/api/questions", questionDto, defaultUser());
        QuestionDto dbQuestion = getResource(location, QuestionDto.class);
        assertThat(questionDto.equals(dbQuestion)).isTrue();

        deleteResource(location, defaultUser());
    }

    @Test
    public void create_Question형식이잘못된경우() {
        QuestionDto questionDto = new QuestionDto("t", "content");
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/api/questions", questionDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void update() {
        QuestionDto questionDto = new QuestionDto("title", "content");
        QuestionDto updateQuestion = new QuestionDto("update", "update content");

        String location = createResource("/api/questions", questionDto, defaultUser());
        basicAuthTemplate(defaultUser()).put(location, updateQuestion);

        QuestionDto dbQuestion = getResource(location, QuestionDto.class);
        assertThat(updateQuestion.equals(dbQuestion)).isTrue();

        deleteResource(location, defaultUser());
    }

    @Test
    public void update_권한이없는사용자() {
        QuestionDto questionDto = new QuestionDto("title", "content");
        QuestionDto updateQuestion = new QuestionDto("update", "update content");

        String location = createResource("/api/questions", questionDto, defaultUser());
        basicAuthTemplate(findByUserId("sanjigi")).put(location, updateQuestion);

        QuestionDto dbQuestion = getResource(location, QuestionDto.class);
        assertThat(questionDto.equals(dbQuestion)).isTrue();

        deleteResource(location, defaultUser());
    }

    @Test
    public void delete() {
        QuestionDto questionDto = new QuestionDto("title", "content");

        String location = createResource("/api/questions", questionDto, defaultUser());
        deleteResource(location, defaultUser());

        ResponseEntity<ErrorResponse> response = template().getForEntity(location, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void delete_권한이없는사용자() {
        QuestionDto questionDto = new QuestionDto("title", "content");

        String location = createResource("/api/questions", questionDto, defaultUser());
        deleteResource(location, findByUserId("sanjigi"));

        ResponseEntity<QuestionDto> response = template().getForEntity(location, QuestionDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        deleteResource(location, defaultUser());
    }
}
