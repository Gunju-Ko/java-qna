package codesquad.web.api;

import codesquad.domain.User;
import codesquad.web.dto.UserDto;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApiUserAcceptanceTest extends AcceptanceTest {

    @Test
    public void create() throws Exception {
        UserDto newUser = createUserDto("testuser1");
        ResponseEntity<String> response = template().postForEntity("/api/users", newUser, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        UserDto dbUser = basicAuthTemplate(findByUserId(newUser.getUserId())).getForObject(location, UserDto.class);
        assertThat(dbUser.getUserId(), is(newUser.getUserId()));
    }

    @Test
    public void show_다른_사람() throws Exception {
        UserDto newUser = createUserDto("testuser2");
        ResponseEntity<String> response = template().postForEntity("/api/users", newUser, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        response = basicAuthTemplate(defaultUser()).getForEntity(location, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void update() throws Exception {
        // given
        UserDto newUser = createUserDto("testuser3");
        ResponseEntity<String> response = template().postForEntity("/api/users", newUser, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        // when
        User loginUser = findByUserId(newUser.getUserId());
        UserDto updateUser = createUserDto(newUser.getUserId());
        updateUser.setName("update name");
        basicAuthTemplate(loginUser).put(location, updateUser);

        // then
        UserDto dbUser = basicAuthTemplate(findByUserId(newUser.getUserId())).getForObject(location, UserDto.class);
        assertThat(dbUser.getName(), is(updateUser.getName()));
    }

    @Test
    public void update_다른_사람() throws Exception {
        // given
        UserDto newUser = createUserDto("testuser4");
        ResponseEntity<String> response = template().postForEntity("/api/users", newUser, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        // when
        UserDto updateUser = createUserDto(newUser.getUserId());
        updateUser.setName("update name");
        basicAuthTemplate(defaultUser()).put(location, updateUser);

        UserDto dbUser = basicAuthTemplate(findByUserId(newUser.getUserId())).getForObject(location, UserDto.class);
        assertThat(dbUser.getName(), is(newUser.getName()));
    }

    @Test
    public void createPhoto() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).exchange("/api/users/1/profile/photos", HttpMethod.POST,
                                                                                    getMultipartHttpEntity(), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();
        assertThat(location, is("/images/users/javajigi.png"));

        deleteFile(location);
    }

    @Test
    public void createPhoto_다른사람() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).exchange("/api/users/2/profile/photos", HttpMethod.POST,
                                                                                    getMultipartHttpEntity(), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private UserDto createUserDto(String userId) {
        return UserDto.builder()
                      .userId(userId)
                      .password("password")
                      .name("name")
                      .email("javajigi@slipp.net")
                      .build();
    }

    private HttpEntity<MultiValueMap<String, Object>> getMultipartHttpEntity() {
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("photo", getTestImageFile());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new HttpEntity<>(bodyMap, headers);
    }

    private Resource getTestImageFile() {
        File file = new File("src/test/resources/static/test.png");
        if (!file.exists()) {
            throw new IllegalStateException("테스트 파일을 찾을 수 없습니다");
        }
        return new FileSystemResource(file);
    }

    private void deleteFile(String location) {
        File file = new File("/Users/skp/my/qna" + location);

        if (!file.exists()) {
            throw new IllegalStateException("파일을 찾을 수 없습니다");
        }
        assertThat(file.delete(), is(true));
    }
}
