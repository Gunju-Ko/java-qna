package codesquad.web.api;

import codesquad.dto.AnswerDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    private AnswerDto answerDto;

    @Before
    public void setUp() throws Exception {
        answerDto = new AnswerDto("질문에 대한 답변입니다");
    }

    @Test
    public void show() throws Exception {
        ResponseEntity<AnswerDto> response = template().getForEntity("/api/questions/1/answers/1", AnswerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    public void show_존재하지않는질문() throws Exception {
        ResponseEntity<AnswerDto> response = template().getForEntity("/api/questions/10/answers/1", AnswerDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void show_존재하지않는답변() throws Exception {
        ResponseEntity<AnswerDto> response = template().getForEntity("/api/questions/1/answers/10", AnswerDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void create() throws Exception {
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());
        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(compareAnswerDto(answerDto, dbAnswer)).isTrue();
    }

    @Test
    public void create_잘못된형식() throws Exception {
        AnswerDto answerDto = new AnswerDto("t");
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/api/questions/1/answers",
                                                                                         answerDto,
                                                                                         String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DirtiesContext
    public void update() throws Exception {
        AnswerDto updateAnswer = new AnswerDto("업데이트된 내용");
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());
        basicAuthTemplate(defaultUser()).put(location, updateAnswer);

        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(compareAnswerDto(updateAnswer, dbAnswer)).isTrue();
    }

    @Test
    @DirtiesContext
    public void update_잘못된형식() throws Exception {
        AnswerDto updateAnswer = new AnswerDto("t");
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());
        basicAuthTemplate(defaultUser()).put(location, updateAnswer);

        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(compareAnswerDto(answerDto, dbAnswer)).isTrue();
    }

    @Test
    public void update_잘못된경로() throws Exception {
        AnswerDto updateAnswer = new AnswerDto("업데이트된 내용");
        basicAuthTemplate(defaultUser()).put("/api/questions/10/answers/1", updateAnswer);

        AnswerDto dbAnswer = getResource("/api/questions/1/answers/1", AnswerDto.class);
        assertThat(compareAnswerDto(updateAnswer, dbAnswer)).isFalse();
    }

    private static boolean compareAnswerDto(AnswerDto answerDto, AnswerDto dbAnswer) {
        if (answerDto == null || dbAnswer == null) {
            return false;
        }
        return answerDto.getContents().equals(dbAnswer.getContents());
    }

    @Test
    public void delete() throws Exception {
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());
        deleteResource(location, defaultUser());

        ResponseEntity<ErrorResponse> response = template().getForEntity(location, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void delete_잘못된경로() throws Exception {
        deleteResource("/api/questions/10/answers/1", defaultUser());
        ResponseEntity<AnswerDto> response = template().getForEntity("/api/questions/1/answers/1", AnswerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void delete_권한이없는사용자() throws Exception {
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());
        deleteResource(location, findByUserId("sanjigi"));

        ResponseEntity<AnswerDto> response = template().getForEntity(location, AnswerDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void update_권한이없는사용자() throws Exception {
        AnswerDto updateAnswer = new AnswerDto("업데이트된 내용");
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());
        basicAuthTemplate(findByUserId("sanjigi")).put(location, updateAnswer);

        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(compareAnswerDto(answerDto, dbAnswer)).isTrue();
    }
}