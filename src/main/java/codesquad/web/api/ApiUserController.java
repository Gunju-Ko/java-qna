package codesquad.web.api;

import codesquad.domain.Photo;
import codesquad.domain.User;
import codesquad.dto.UserDto;
import codesquad.security.LoginUser;
import codesquad.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Resource(name = "userService")
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Void> create(@Valid @RequestBody UserDto user) {
        User savedUser = userService.add(user);
        return new ResponseEntity<>(savedUser.makeHttpHeaders(), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public UserDto show(@LoginUser User loginUser, @PathVariable long id) {
        User user = userService.findOne(loginUser, id);
        return user.toUserDto();
    }

    @PutMapping("{id}")
    public void update(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody UserDto updatedUser) {
        userService.update(loginUser, id, updatedUser);
    }

    @PostMapping("/{id}/profile/photos")
    public ResponseEntity<Void> createPhoto(@LoginUser User loginUser,
                                            @PathVariable long id,
                                            @RequestParam("photo") MultipartFile multipartFile) {
        Photo photo = userService.addPhoto(loginUser, id, multipartFile);

        return new ResponseEntity<>(photo.makeHttpHeaders(), HttpStatus.CREATED);
    }
}
