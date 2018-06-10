package codesquad.service;

import codesquad.common.exception.FileCreateException;
import codesquad.common.exception.FileDeleteException;
import codesquad.common.utils.PhotoUtils;
import codesquad.domain.ImageFormat;
import codesquad.domain.Photo;
import codesquad.domain.User;
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
        return Photo.of(PhotoUtils.userPhotoLocation(file));
    }

    private File createFile(User user, ImageFormat imageFormat) {
        String absoluteFilePath = PhotoUtils.userPhotoAbsolutePath(user, imageFormat);
        deleteFile(absoluteFilePath);
        File file = new File(absoluteFilePath);

        createParentDirectoryIfNeed(file);

        return file;
    }

    private void createParentDirectoryIfNeed(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (parent.mkdirs()) {
                throw new FileCreateException("부모 디렉토리 생성 실패");
            }
        }
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
