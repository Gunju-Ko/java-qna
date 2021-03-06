package codesquad.web.api;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.service.QnaService;
import codesquad.web.dto.AnswerDto;
import codesquad.web.security.LoginUser;
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
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    private final QnaService qnaService;

    public ApiAnswerController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/{id}")
    public AnswerDto show(@PathVariable long id) {
        return qnaService.findAnswerByIdAndNotDeleted(id)
                         .toAnswerDto();
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser,
                                       @PathVariable long questionId,
                                       @RequestBody @Valid AnswerDto answerDto) {
        Answer answer = answerDto.toAnswer();
        answer.writerBy(loginUser);

        Answer result = qnaService.addAnswer(answer, questionId);
        return new ResponseEntity<>(result.makeHttpHeaders(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@LoginUser User loginUser,
                                       @PathVariable long id,
                                       @RequestBody @Valid AnswerDto answerDto) {
        Answer answer = answerDto.toAnswer();
        answer.writerBy(loginUser);

        Answer result = qnaService.updateAnswer(id, answer);
        return new ResponseEntity<>(result.makeHttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUser User loginUser,
                                       @PathVariable long id) {
        qnaService.deleteAnswer(loginUser, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
