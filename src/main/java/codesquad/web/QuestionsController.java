package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/questions")
public class QuestionsController {

    @GetMapping("/form")
    public String form() {
        return "/qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser,
                         @Valid QuestionDto questionDto) {
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id) {

        return "/qna/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser,
                         @PathVariable long id) {

        return "redirect:/";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser,
                         @PathVariable long id,
                         QuestionDto questionDto) {

        return "redirect:/";
    }
}
