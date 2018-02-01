package codesquad.service;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerService {
    public Optional<Answer> findByIdAndQuestion(long id, Question question) {
        return Optional.empty();
    }

    public Answer create(Answer answer, User loginUser, Question question) {
        return null;
    }

    public Answer update(User loginUser, long id, Answer answer) {
        return null;
    }

    public void delete(User loginUser, long id) {

    }
}
