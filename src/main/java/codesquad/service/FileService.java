package codesquad.service;

import codesquad.FileCreateException;
import codesquad.FileDeleteException;
import codesquad.domain.ImageFormat;
import codesquad.domain.Photo;
import codesquad.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private final String resourcePath = "src/main/resources/static/images/users";

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
        return new Photo(file.getPath());
    }

    private File createFile(User user, ImageFormat imageFormat) {
        String filePath = String.format("%s/%s.%s", resourcePath, user.getUserId(), imageFormat.toString());
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
}
