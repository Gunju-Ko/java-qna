package codesquad.web;

import codesquad.QuestionNotFoundException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.http.HttpHeaders;
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
import java.net.URI;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {

    private final QnaService qnaService;

    public ApiQuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> show(@PathVariable long id) {
        Question question = qnaService.findByIdAndNotDeleted(id)
                                      .orElseThrow(() -> new QuestionNotFoundException(id));
        return ResponseEntity.ok(question.toQuestionDto());
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser,
                                       @RequestBody @Valid QuestionDto questionDto) {
        Question question = qnaService.create(loginUser, questionDto.toQuestion());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + question.getId()));

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@LoginUser User loginUser,
                                       @PathVariable long id,
                                       @RequestBody @Valid QuestionDto questionDto) {
        Question question = qnaService.update(loginUser, id, questionDto.toQuestion());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + question.getId()));

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUser User loginUser,
                                       @PathVariable long id) {
        qnaService.deleteQuestion(loginUser, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
