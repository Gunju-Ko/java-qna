package codesquad.web;

import codesquad.QuestionNotFoundException;
import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private final QuestionService questionService;

    public QuestionsController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/form")
    public String form() {
        return "/qna/form";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable long id, Model model) {
        if (!questionService.isOwnerOfQuestion(loginUser, id)) {
            throw new UnAuthorizedException();
        }
        Question question = questionService.findByIdAndNotDeleted(id)
                                           .orElseThrow(() -> new QuestionNotFoundException(id));

        model.addAttribute("question", question);
        return "/qna/updateForm";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        Question question = questionService.findByIdAndNotDeleted(id)
                                           .orElseThrow(() -> new QuestionNotFoundException(id));

        model.addAttribute("question", question);
        return "/qna/show";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser,
                         @Valid QuestionDto questionDto) {
        questionService.create(loginUser, questionDto.toQuestion());

        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser,
                         @PathVariable long id) {
        questionService.deleteQuestion(loginUser, id);

        return "redirect:/";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser,
                         @PathVariable long id,
                         @Valid QuestionDto questionDto) {
        Question question = questionService.update(loginUser, id, questionDto.toQuestion());
        return "redirect:" + question.generateUrl();
    }
}
