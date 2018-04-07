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

    private static final String PHOTO_RESOURCE_PATH = "src/main/resources/static/images/users";

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

    private File createFile(User user, ImageFormat imageFormat) {
        String filePath = String.format("%s/%s.%s", PHOTO_RESOURCE_PATH, user.getUserId(), imageFormat.toString());
        deleteFile(filePath);

        File f = new File(filePath);
        return new File(f.getAbsolutePath());
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        if (!file.delete()) {
            throw new FileDeleteException(filePath);
        }
    }

    private String imagePath(File file) {
        String absolutePath = file.getAbsolutePath();

        String[] s = StringUtils.split(absolutePath, "/");
        if (s.length <= 3) {
            throw new IllegalArgumentException("이미지 경로가 올바르지 못합니다 : " + absolutePath);
        }

        int last = s.length - 1;
        return String.format("/%s/%s/%s", s[last - 2], s[last - 1], s[last]);
    }
}
