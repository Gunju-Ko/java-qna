package codesquad.common.utils;

import codesquad.domain.ImageFormat;
import codesquad.domain.User;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class PhotoUtilsTest {
    @Test
    public void getUserImagePath() throws Exception {
        User user = User.builder()
                        .userId("test")
                        .password("password")
                        .name("name")
                        .email("test@slipp.net")
                        .build();

        assertThat(PhotoUtils.userPhotoAbsolutePath(user,
                                                    ImageFormat.JPG)).isEqualTo("/Users/skp/my/qna/images/users/test.jpg");
    }

    @Test
    public void imagePath() throws Exception {
        String imagePath = PhotoUtils.userPhotoLocation(new File("/Users/skp/my/qna/images/users/test.jpg"));
        assertThat(imagePath).isEqualTo("/images/users/test.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void imagePath_경로가올바르지않는경우() throws Exception {
        PhotoUtils.userPhotoLocation(new File("/images/test.png"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void imagePath_경로가올바르지않는경우2() throws Exception {
        PhotoUtils.userPhotoLocation(new File("src/main/resources/static/template/test.png"));
    }
}