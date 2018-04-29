package codesquad.web;

import codesquad.domain.User;
import codesquad.service.UserService;
import codesquad.web.dto.UpdateUserDto;
import codesquad.web.dto.UserDto;
import codesquad.web.security.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Validated
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/form")
    public String form() {
        return "/user/form";
    }

    @PostMapping("")
    public String create(@Valid UserDto userDto) {
        userService.add(userDto);
        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable long id, Model model) {
        model.addAttribute("user", userService.findOne(loginUser, id));
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, @Valid UpdateUserDto target) {
        if (!target.isValidUpdatePassword()) {
            throw new IllegalArgumentException("새로운 비밀번호가 올바르지 않습니다");
        }
        userService.update(loginUser, id, target);
        return "redirect:/users";
    }

    @GetMapping("/{id}/profile")
    public String profile(@LoginUser User loginUser, @PathVariable long id, Model model) {
        model.addAttribute("user", userService.findOne(loginUser, id));
        return "/user/profile";
    }

    @PostMapping("/{id}/profile/photos")
    public String createPhoto(@LoginUser User loginUser,
                              @PathVariable long id,
                              @RequestParam("photo") MultipartFile multipartFile) {
        userService.addPhoto(loginUser, id, multipartFile);
        return "redirect:/users/" + id + "/form";
    }

}
