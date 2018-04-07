package codesquad.common.utils;

import codesquad.domain.ImageFormat;
import codesquad.domain.User;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class UserPhotoUtils {

    private static final String IMAGE_DIRECTORY = "images";

    private static final String USER_DIRECTORY = "users";

    private static final String PHOTO_RESOURCE_PATH = String.format("src/main/resources/static/%s/%s", IMAGE_DIRECTORY, USER_DIRECTORY);

    public static String userPhotoRelativePath(User user, ImageFormat imageFormat) {
        return String.format("%s/%s.%s", PHOTO_RESOURCE_PATH, user.getUserId(), imageFormat.toString());
    }

    public static String userPhotoLocation(File file) {
        String absolutePath = file.getAbsolutePath();

        String[] directories = StringUtils.split(absolutePath, "/");
        if (directories.length <= 3) {
            throw new IllegalArgumentException("이미지 경로가 올바르지 못합니다 : " + absolutePath);
        }

        int last = directories.length - 1;
        if (!directories[last - 2].equals(IMAGE_DIRECTORY) || !directories[last - 1].equals(USER_DIRECTORY)) {
            throw new IllegalArgumentException("이미지 경로가 올바르지 못합니다");
        }
        return String.format("/%s/%s/%s", IMAGE_DIRECTORY, USER_DIRECTORY, directories[last]);
    }

    public static String absolutePath(String path) {
        File file = new File(path);
        return file.getAbsolutePath();
    }
}
