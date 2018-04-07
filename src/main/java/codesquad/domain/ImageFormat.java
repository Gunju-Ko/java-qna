package codesquad.domain;

import org.apache.commons.io.FilenameUtils;

import java.util.Arrays;

public enum ImageFormat {
    PNG("png"),
    JPEG("jpeg"),
    JPG("jpg");

    private final String format;

    ImageFormat(String format) {
        this.format = format;
    }

    public static boolean support(String fileName) {
        String format = getFileFormat(fileName);
        return Arrays.stream(values())
                     .anyMatch(f -> f.format.equals(format));
    }

    public static ImageFormat fromString(String fileName) {
        String format = getFileFormat(fileName);

        return Arrays.stream(values())
                     .filter(f -> f.format.equals(format))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }

    private static String getFileFormat(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    @Override
    public String toString() {
        return format;
    }
}
