package codesquad.web;

import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.UserTestMother;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static support.test.HttpEntityUtils.makeFormUrlEncodedRequest;

public class LoginAcceptanceTest extends AcceptanceTest {

    @Test
    public void login() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/login", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void login_성공() throws Exception {
        User javajigi = UserTestMother.javajigi();

        ResponseEntity<String> response = template().postForEntity("/login", makeFormUrlEncodedRequest(javajigi), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
    }

    @Test
    public void login_패스워드가다른경우() throws Exception {
        User javajigi = User.builder()
                            .userId("javajigi")
                            .password("test2")  // wrong password
                            .name("자바지기")
                            .email("javajigi@slipp.net")
                            .build();

        ResponseEntity<String> response = template().postForEntity("/login", makeFormUrlEncodedRequest(javajigi), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void login_아이디가없는경우() throws Exception {
        User javajigi = User.builder()
                            .userId(null)
                            .password("test2")
                            .name("자바지기")
                            .email("javajigi@slipp.net")
                            .build();
        ResponseEntity<String> response = template().postForEntity("/login", makeFormUrlEncodedRequest(javajigi), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void login_존재하지않는회원() throws Exception {
        User user = User.builder()
                        .userId("gyusun")
                        .password("test")
                        .name("규선이")
                        .email("gyusun@slipp.net")
                        .build();

        ResponseEntity<String> response = template().postForEntity("/login", makeFormUrlEncodedRequest(user), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void logout() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/logout", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

}
