package codesquad.web;

import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.web.dto.UpdateUserDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import support.test.AcceptanceTest;
import support.test.HttpEntityUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static support.test.HttpEntityUtils.makeFormUrlEncodedRequest;

public class UserAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createForm() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/users/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void create() throws Exception {
        User user = new User("testuser", "password", "자바지기", "javajigi@slipp.net");

        ResponseEntity<String> response = template().postForEntity("/users", makeFormUrlEncodedRequest(user), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertNotNull(userRepository.findByUserId(user.getUserId()));
        assertThat(response.getHeaders().getLocation().getPath(), is("/users"));
    }

    @Test
    public void list() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/users", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().contains(defaultUser().getEmail()), is(true));
    }

    @Test
    public void updateForm_no_login() throws Exception {
        ResponseEntity<String> response = template().getForEntity(String.format("/users/%d/form", defaultUser().getId()),
                                                                  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void updateForm_login() throws Exception {
        User loginUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loginUser)
            .getForEntity(String.format("/users/%d/form", loginUser.getId()), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().contains(loginUser.getEmail()), is(true));
    }

    @Test
    @DirtiesContext
    public void update() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                                                   .userId("javajigi")
                                                   .password("test")
                                                   .name("자바지기2")
                                                   .email("javajigi2@slipp.net")
                                                   .updatePassword("12345")
                                                   .confirmUpdatePassword("12345")
                                                   .build();

        ResponseEntity<String> response = update(basicAuthTemplate(),
                                                 HttpEntityUtils.makeFormUrlEncodedRequest(updateUserDto));
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertTrue(response.getHeaders().getLocation().getPath().startsWith("/users"));

        User dbUser = findByUserId("javajigi");
        assertThat(dbUser.getName(), is("자바지기2"));
        assertThat(dbUser.getEmail(), is("javajigi2@slipp.net"));
        assertThat(dbUser.getPassword(), is("12345"));
    }

    @Test(expected = ResourceAccessException.class)
    public void update_no_login() throws Exception {
        ResponseEntity<String> response = update(template(), HttpEntityUtils.makeFormUrlEncodedRequest(defaultUser()));
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void update_잘못된새로운비밀번호() throws Exception {
        UpdateUserDto updateUser = UpdateUserDto.builder()
                                                .userId("javajigi")
                                                .password("test")
                                                .name("자바지기")
                                                .email("javajigi@slipp.net")
                                                .updatePassword("12345")
                                                .confirmUpdatePassword("1234")
                                                .build();

        ResponseEntity<String> response = update(basicAuthTemplate(), HttpEntityUtils.makeFormUrlEncodedRequest(updateUser));
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void profile() throws Exception {
        User loginUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loginUser)
            .getForEntity(String.format("/users/%d/profile", loginUser.getId()), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void profile_no_login() throws Exception {
        User loginUser = defaultUser();
        ResponseEntity<String> response = template()
            .getForEntity(String.format("/users/%d/profile", loginUser.getId()), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void profile_inValidId() throws Exception {
        User loginUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loginUser)
            .getForEntity("/users/2/profile", String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private ResponseEntity<String> update(TestRestTemplate template, HttpEntity<MultiValueMap<String, Object>> httpEntity) {
        String url = String.format("/users/%d", defaultUser().getId());

        return template.exchange(url, HttpMethod.PUT, httpEntity, String.class);
    }
}
