package codesquad.web.api;

import codesquad.AnswerNotFoundException;
import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    private final AnswerService answerService;

    public ApiAnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/{id}")
    public AnswerDto show(@PathVariable("questionId") @NotNull Question question,
                          @PathVariable long id) {
        return answerService.findByIdAndQuestion(id, question)
                            .orElseThrow(() -> new AnswerNotFoundException(id))
                            .toAnswerDto();
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser,
                                       @PathVariable("questionId") @NotNull Question question,
                                       @RequestBody @Valid AnswerDto answerDto) {
        Answer answer = answerService.create(answerDto.toAnswer(loginUser), question);
        return new ResponseEntity<>(answer.makeHttpHeaders(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@LoginUser User loginUser,
                                       @PathVariable("questionId") @NotNull Question question,
                                       @PathVariable long id,
                                       @RequestBody @Valid AnswerDto answerDto) {
        if (!question.containAnswer(id)) {
            throw new AnswerNotFoundException(id);
        }
        Answer answer = answerService.update(id, answerDto.toAnswer(loginUser));
        return new ResponseEntity<>(answer.makeHttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUser User loginUser,
                                       @PathVariable("questionId") @NotNull Question question,
                                       @PathVariable long id) {
        if (!question.containAnswer(id)) {
            throw new AnswerNotFoundException(id);
        }
        answerService.delete(loginUser, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
