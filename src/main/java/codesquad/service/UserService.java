package codesquad.service;

import codesquad.common.exception.PermissionDeniedException;
import codesquad.common.exception.UnAuthenticationException;
import codesquad.common.exception.UserNotFoundException;
import codesquad.domain.Photo;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.web.dto.UpdateUserDto;
import codesquad.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final FileService fileService;

    public User add(UserDto userDto) {
        return userRepository.save(userDto.toUser());
    }

    @Transactional
    public void update(User loginUser, long id, UpdateUserDto updatedUser) {
        User original = userRepository.findOne(id);
        original.update(loginUser, updatedUser.toUser(), updatedUser.getUpdatePassword());
    }

    public User findOne(User loginUser, long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        if (!user.equals(loginUser)) {
            throw new PermissionDeniedException();
        }
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User login(String userId, String password) throws UnAuthenticationException {
        if (userId == null || password == null) {
            throw new UnAuthenticationException();
        }
        User user = userRepository.findByUserId(userId)
                                  .orElseThrow(UnAuthenticationException::new);
        if (!user.matchPassword(password)) {
            throw new UnAuthenticationException();
        }
        return user;
    }

    @Transactional
    public Photo addPhoto(User loginUser, long id, MultipartFile multipartFile) {
        User user = findOne(loginUser, id);
        Photo photo = fileService.savePhoto(user, multipartFile);
        user.setPhoto(photo);

        return photo;
    }
}
