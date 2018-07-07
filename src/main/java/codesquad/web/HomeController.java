package codesquad.web;

import codesquad.domain.Question;
import codesquad.service.QnaService;
import codesquad.web.dto.Paged;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final QnaService qnaService;

    public HomeController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/")
    public String home(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<Question> questions = qnaService.findAll(pageable);

        model.addAttribute("questions", Paged.of(questions, pageable, "/").map(Question::toQuestionDto));
        return "home";
    }
}
