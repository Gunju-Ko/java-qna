package codesquad.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageFormatTest {

    @Test
    public void support() throws Exception {
        assertThat(ImageFormat.support("test.png")).isTrue();
        assertThat(ImageFormat.support("test.jpg")).isTrue();
        assertThat(ImageFormat.support("test.jpeg")).isTrue();
    }

    @Test
    public void support_지원하지않는포맷() throws Exception {
        assertThat(ImageFormat.support("test.txt")).isFalse();
    }

    @Test
    public void fromString() throws Exception {
        assertThat(ImageFormat.fromString("test.png")).isEqualTo(ImageFormat.PNG);
        assertThat(ImageFormat.fromString("test.jpg")).isEqualTo(ImageFormat.JPG);
        assertThat(ImageFormat.fromString("test.jpeg")).isEqualTo(ImageFormat.JPEG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromString_지원하지않는포맷() throws Exception {
        ImageFormat.fromString("test.txt");
    }

}