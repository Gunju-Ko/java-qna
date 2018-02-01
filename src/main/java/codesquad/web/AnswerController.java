package codesquad.web;

import codesquad.AnswerNotFoundException;
import codesquad.UnAuthorizedException;
import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/{id}/form")
    public String showForm(@LoginUser User loginUser,
                           @PathVariable("questionId") @NotNull Question question,
                           @PathVariable long id,
                           Model model) {
        if (!answerService.isOwnerOfAnswer(loginUser, id)) {
            throw new UnAuthorizedException();
        }
        Answer answer = answerService.findByIdAndQuestion(id, question)
                                     .orElseThrow(() -> new AnswerNotFoundException(id));
        model.addAttribute("answer", answer);
        return "/qna/answerUpdateForm";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser,
                         @PathVariable("questionId") @NotNull Question question,
                         @Valid AnswerDto answerDto) {
        answerService.create(answerDto.toAnswer(loginUser), question);
        return "redirect:/questions/" + question.getId();
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser,
                         @PathVariable("questionId") @NotNull Question question,
                         @PathVariable long id,
                         @Valid AnswerDto answerDto) {
        if (!question.containAnswer(id)) {
            throw new AnswerNotFoundException(id);
        }
        answerService.update(id, answerDto.toAnswer(loginUser));
        return "redirect:/questions/" + question.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser,
                         @PathVariable("questionId") @NotNull Question question,
                         @PathVariable long id) {
        if (!question.containAnswer(id)) {
            throw new AnswerNotFoundException(id);
        }
        answerService.delete(loginUser, id);
        return "redirect:/questions/" + question.getId();
    }
}
