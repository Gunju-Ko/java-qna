package codesquad.web.api;

import codesquad.common.exception.AnswerNotFoundException;
import codesquad.common.exception.CannotDeleteException;
import codesquad.common.exception.InvalidPasswordException;
import codesquad.common.exception.QuestionNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RestApiControllerAcceptanceTest {

    @Mock
    private static Fake fake;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:messages");
        source.setDefaultEncoding("UTF-8");
        MessageSourceAccessor accessor = new MessageSourceAccessor(source);
        mvc = MockMvcBuilders.standaloneSetup(new TestController())
                             .setControllerAdvice(new RestApiControllerAdvice(accessor))
                             .build();
    }

    @Test
    public void invalidPasswordException() throws Exception {
        assertThatPerformExceptionHandle(new InvalidPasswordException(), HttpStatus.FORBIDDEN, "잘못된 패스워드 입니다. 다시 한 번 확인해주세요");
    }

    @Test
    public void QuestionNotFoundException() throws Exception {
        assertThatPerformExceptionHandle(new QuestionNotFoundException(1), HttpStatus.NOT_FOUND, "해당 질문이 삭제되었거나 존재하지 않습니다 : 1");
    }

    @Test
    public void AnswerNotFoundException() throws Exception {
        assertThatPerformExceptionHandle(new AnswerNotFoundException(1), HttpStatus.NOT_FOUND, "해당 답변이 삭제되었거나 존재하지 않습니다 : 1");
    }

    @Test
    public void CannotDeleteException() throws Exception {
        assertThatPerformExceptionHandle(new CannotDeleteException(), HttpStatus.FORBIDDEN, "해당 질문은 삭제가 불가능 합니다.");
    }

    public void assertThatPerformExceptionHandle(RuntimeException exception,
                                                 HttpStatus status,
                                                 String expectedMessage) throws Exception {
        doThrow(exception).when(fake)
                          .action();
        mvc.perform(get("/test"))
           .andExpect(status().is(status.value()))
           .andExpect(jsonPath("$.message", is(expectedMessage)));

    }

    interface Fake {
        String action() throws Exception;
    }

    @RestController
    static class TestController {
        @GetMapping("test")
        public String test() throws Exception {
            return fake.action();
        }
    }
}
