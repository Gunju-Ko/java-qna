package codesquad.web.security;

import codesquad.domain.User;
import codesquad.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import support.UserTestMother;

import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicAuthInterceptorTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private BasicAuthInterceptor basicAuthInterceptor;

    @Test
    public void preHandle_로그인_성공() throws Exception {
        User loginUser = UserTestMother.javajigi();

        MockHttpServletRequest request = basicAuthHttpRequest(loginUser.getUserId(), loginUser.getPassword());
        when(userService.login(loginUser.getUserId(), loginUser.getPassword())).thenReturn(loginUser);

        basicAuthInterceptor.preHandle(request, null, null);
        assertThat(request.getSession().getAttribute(HttpSessionUtils.USER_SESSION_KEY), is(loginUser));
    }

    private MockHttpServletRequest basicAuthHttpRequest(String userId, String password) {
        String encodedBasicAuth = Base64.getEncoder()
                                        .encodeToString(String.format("%s:%s", userId, password).getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + encodedBasicAuth);
        return request;
    }
}
