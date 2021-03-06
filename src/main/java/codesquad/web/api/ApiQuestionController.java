package codesquad.web.api;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.service.QnaService;
import codesquad.web.dto.Paged;
import codesquad.web.dto.QuestionDto;
import codesquad.web.security.LoginUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    private final QnaService qnaService;

    public ApiQuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("")
    public ResponseEntity<Paged<QuestionDto>> showPage(@PageableDefault Pageable pageable) {
        Page<Question> questions = qnaService.findAll(pageable);

        return ResponseEntity.ok(Paged.of(questions, pageable, "/api/questions").map(Question::toQuestionDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> show(@PathVariable long id,
                                            @PageableDefault(size = 5) Pageable pageable) {
        Question question = qnaService.findQuestionByIdAndNotDeleted(id);
        Page<Answer> answers = qnaService.findAnswerByQuestion(question, pageable);

        QuestionDto result = question.toQuestionDto();
        result.setAnswers(Paged.of(answers, pageable, "/api/questions/" + id).map(Answer::toAnswerDto));

        return ResponseEntity.ok(result);
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser,
                                       @RequestBody @Valid QuestionDto questionDto) {
        Question question = qnaService.create(loginUser, questionDto.toQuestion());
        return new ResponseEntity<>(question.makeHttpHeaders(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@LoginUser User loginUser,
                                       @PathVariable long id,
                                       @RequestBody @Valid QuestionDto questionDto) {
        Question question = qnaService.update(loginUser, id, questionDto.toQuestion());
        return new ResponseEntity<>(question.makeHttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUser User loginUser,
                                       @PathVariable long id) {
        qnaService.deleteQuestion(loginUser, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
