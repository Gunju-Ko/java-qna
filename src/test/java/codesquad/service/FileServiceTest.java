package codesquad.service;

import codesquad.domain.ImageFormat;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class FileServiceTest {

    private FileService fileService;

    @Before
    public void setUp() throws Exception {
        fileService = new FileService();
    }

    @Test
    public void getUserImagePath() throws Exception {
        User user = new User("test", "password", "name", "email");
        assertThat(fileService.getUserPhotoPath(user, ImageFormat.JPG)).isEqualTo("src/main/resources/static/images/users/test.jpg");
    }

    @Test
    public void imagePath() throws Exception {
        String imagePath = fileService.imagePath(new File("src/main/resources/static/images/users/test.png"));
        assertThat(imagePath).isEqualTo("/images/users/test.png");
    }

    @Test(expected = IllegalArgumentException.class)
    public void imagePath_경로가올바르지않는경우() throws Exception {
        fileService.imagePath(new File("/images/test.png"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void imagePath_경로가올바르지않는경우2() throws Exception {
        fileService.imagePath(new File("src/main/resources/static/template/test.png"));
    }

    @Test
    public void getAbsolutePath() throws Exception {
        String absolutePath = fileService.getAbsolutePath("src/main/resources/static/images/users/test.png");
        assertThat(absolutePath).isEqualTo("/Users/skp/IdeaProjects/java-qna/src/main/resources/static/images/users/test.png");
    }
}