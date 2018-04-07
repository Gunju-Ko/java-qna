package codesquad.common.utils;

import codesquad.domain.ImageFormat;
import codesquad.domain.User;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class UserPhotoUtilsTest {
    @Test
    public void getUserImagePath() throws Exception {
        User user = new User("test", "password", "name", "email");
        assertThat(UserPhotoUtils.userPhotoRelativePath(user,
                                                        ImageFormat.JPG)).isEqualTo("src/main/resources/static/images/users/test.jpg");
    }

    @Test
    public void imagePath() throws Exception {
        String imagePath = UserPhotoUtils.userPhotoLocation(new File("src/main/resources/static/images/users/test.png"));
        assertThat(imagePath).isEqualTo("/images/users/test.png");
    }

    @Test(expected = IllegalArgumentException.class)
    public void imagePath_경로가올바르지않는경우() throws Exception {
        UserPhotoUtils.userPhotoLocation(new File("/images/test.png"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void imagePath_경로가올바르지않는경우2() throws Exception {
        UserPhotoUtils.userPhotoLocation(new File("src/main/resources/static/template/test.png"));
    }

    @Test
    public void getAbsolutePath() throws Exception {
        String absolutePath = UserPhotoUtils.absolutePath("src/main/resources/static/images/users/test.png");
        assertThat(absolutePath).isEqualTo("/Users/skp/IdeaProjects/java-qna/src/main/resources/static/images/users/test.png");
    }
}