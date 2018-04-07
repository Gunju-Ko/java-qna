package codesquad.service;

import codesquad.FileCreateException;
import codesquad.FileDeleteException;
import codesquad.domain.ImageFormat;
import codesquad.domain.Photo;
import codesquad.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private static final String IMAGE_DIRECTORY = "images";

    private static final String USER_DIRECTORY = "users";

    private static final String PHOTO_RESOURCE_PATH = String.format("src/main/resources/static/%s/%s", IMAGE_DIRECTORY, USER_DIRECTORY);

    public Photo savePhoto(User user, MultipartFile multipartFile) {
        if (!ImageFormat.support(multipartFile.getOriginalFilename())) {
            throw new IllegalArgumentException("해당 포맷은 지원하지 않습니다 (jpg, jpeg, png 지원)");
        }
        File file = createFile(user, ImageFormat.fromString(multipartFile.getOriginalFilename()));
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileCreateException(e.getMessage());
        }
        return new Photo(imagePath(file));
    }

    File createFile(User user, ImageFormat imageFormat) {
        String filePath = getUserPhotoPath(user, imageFormat);
        deleteFile(filePath);
        return new File(getAbsolutePath(filePath));
    }

    void deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        if (!file.delete()) {
            throw new FileDeleteException(filePath);
        }
    }

    String getUserPhotoPath(User user, ImageFormat imageFormat) {
        return String.format("%s/%s.%s", PHOTO_RESOURCE_PATH, user.getUserId(), imageFormat.toString());
    }

    String imagePath(File file) {
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

    String getAbsolutePath(String path) {
        File file = new File(path);
        return file.getAbsolutePath();
    }
}
