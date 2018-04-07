package codesquad.service;

import codesquad.FileCreateException;
import codesquad.FileDeleteException;
import codesquad.domain.ImageFormat;
import codesquad.domain.Photo;
import codesquad.domain.User;
import codesquad.utils.UserPhotoUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

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
        return new Photo(UserPhotoUtils.userPhotoLocation(file));
    }

    private File createFile(User user, ImageFormat imageFormat) {
        String filePath = UserPhotoUtils.userPhotoRelativePath(user, imageFormat);
        deleteFile(filePath);
        return new File(UserPhotoUtils.absolutePath(filePath));
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
