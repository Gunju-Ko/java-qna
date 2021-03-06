package codesquad.web.api;

import codesquad.web.dto.AnswerDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    private AnswerDto answerDto;

    @Before
    public void setUp() throws Exception {
        answerDto = AnswerDto.builder()
                             .contents("질문에 대한 답변입니다")
                             .build();
    }

    @Test
    public void show() throws Exception {
        ResponseEntity<AnswerDto> response = template().getForEntity("/api/questions/1/answers/1", AnswerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void show_존재하지않는답변() throws Exception {
        ResponseEntity<AnswerDto> response = template().getForEntity("/api/questions/1/answers/10", AnswerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void create() throws Exception {
        // when
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());

        // then
        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(dbAnswer.getContents()).isEqualTo(answerDto.getContents());
        deleteResource(location, defaultUser());
    }

    @Test
    public void create_잘못된형식() throws Exception {
        // given
        AnswerDto answerDto = new AnswerDto("t");

        // when
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/api/questions/1/answers",
                                                                                         answerDto,
                                                                                         String.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void update() throws Exception {
        // given
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());

        // when
        AnswerDto updateAnswer = new AnswerDto("업데이트된 내용");
        basicAuthTemplate(defaultUser()).put(location, updateAnswer);

        // then
        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(dbAnswer.getContents()).isEqualTo(updateAnswer.getContents());

        deleteResource(location, defaultUser());
    }

    @Test
    public void update_잘못된형식() throws Exception {
        // given
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());

        // when
        AnswerDto updateAnswer = new AnswerDto("t");
        basicAuthTemplate(defaultUser()).put(location, updateAnswer);

        // then
        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(dbAnswer.getContents()).isNotEqualTo(updateAnswer.getContents());
        assertThat(dbAnswer.getContents()).isEqualTo(answerDto.getContents());

        deleteResource(location, defaultUser());
    }

    @Test
    public void delete() throws Exception {
        // given
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());

        // when
        deleteResource(location, defaultUser());

        // then
        ResponseEntity<ErrorResponse> response = template().getForEntity(location, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void delete_권한이없는사용자() throws Exception {
        // given
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());

        // when
        deleteResource(location, findByUserId("sanjigi"));

        // then
        ResponseEntity<AnswerDto> response = template().getForEntity(location, AnswerDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        deleteResource(location, defaultUser());
    }

    @Test
    public void update_권한이없는사용자() throws Exception {
        // given
        String location = createResource("/api/questions/1/answers", answerDto, defaultUser());

        // when
        AnswerDto updateAnswer = new AnswerDto("업데이트된 내용");
        basicAuthTemplate(findByUserId("sanjigi")).put(location, updateAnswer);

        // then
        AnswerDto dbAnswer = getResource(location, AnswerDto.class);
        assertThat(dbAnswer.getContents()).isNotEqualTo(updateAnswer.getContents());
        assertThat(dbAnswer.getContents()).isEqualTo(answerDto.getContents());

        deleteResource(location, defaultUser());
    }
}