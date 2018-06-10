package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.web.dto.QuestionDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import support.UserTestMother;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static support.test.HttpEntityUtils.makeFormUrlEncodedRequest;

public class QuestionAcceptanceTest extends AcceptanceTest {

    @Autowired
    private QuestionRepository repository;

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
    public void questionsForm() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    @DirtiesContext
    public void create() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions",
                                                                                         makeFormUrlEncodedRequest(question),
                                                                                         String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(repository.findByWriter(defaultUser()).size(), is(3));
    }

    @Test
    public void create_잘못된질문형식_TITLE이3보다작은경우() throws Exception {
        question.setTitle("t");

        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions",
                                                                                         makeFormUrlEncodedRequest(question),
                                                                                         String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void show() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/1", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void show_존재하지않는질문() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/10", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void updateForm() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity("/questions/1/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void updateForm_권한이없는사용자() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(UserTestMother.sanjigi()).getForEntity("/questions/1/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void updateForm_존재하지않는질문() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity("/questions/10/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    @DirtiesContext
    public void update() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).exchange("/questions/1",
                                                                                    HttpMethod.PUT,
                                                                                    makeFormUrlEncodedRequest(updateQuestion),
                                                                                    String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        Question question = repository.findOne(1L);

        assertThat(question.getTitle(), is(updateQuestion.getTitle()));
        assertThat(question.getContents(), is(updateQuestion.getContents()));
    }

    @Test
    public void update_권한이없는사용자() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(UserTestMother.sanjigi()).exchange("/questions/1",
                                                                                               HttpMethod.PUT,
                                                                                               makeFormUrlEncodedRequest(updateQuestion),
                                                                                               String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    @DirtiesContext
    public void delete() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).exchange("/questions/4", HttpMethod.DELETE,
                                                                                    HttpEntity.EMPTY, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        Question question = repository.findOne(4L);
        assertThat(question.isDeleted(), is(true));
    }

    @Test
    public void delete_답변에다른답변자가존재하는경우() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).exchange("/questions/1", HttpMethod.DELETE,
                                                                                    HttpEntity.EMPTY, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void delete_권한이없는사용자() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(UserTestMother.sanjigi()).exchange("/questions/1", HttpMethod.DELETE,
                                                                                               HttpEntity.EMPTY, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}
