package codesquad.web.api;

import codesquad.QuestionNotFoundException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {

    private final QuestionService questionService;

    public ApiQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> show(@PathVariable long id) {
        Question question = questionService.findByIdAndNotDeleted(id)
                                           .orElseThrow(() -> new QuestionNotFoundException(id));
        return ResponseEntity.ok(question.toQuestionDto());
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser,
                                       @RequestBody @Valid QuestionDto questionDto) {
        Question question = questionService.create(loginUser, questionDto.toQuestion());
        return new ResponseEntity<>(question.makeHttpHeaders(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@LoginUser User loginUser,
                                       @PathVariable long id,
                                       @RequestBody @Valid QuestionDto questionDto) {
        Question question = questionService.update(loginUser, id, questionDto.toQuestion());
        return new ResponseEntity<>(question.makeHttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUser User loginUser,
                                       @PathVariable long id) {
        questionService.deleteQuestion(loginUser, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
