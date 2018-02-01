package codesquad.service;

import codesquad.AnswerNotFoundException;
import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AnswerService {

    private final AnswerRepository repository;

    public AnswerService(AnswerRepository repository) {
        this.repository = repository;
    }

    public Optional<Answer> findByIdAndQuestion(long id, Question question) {
        return Optional.ofNullable(repository.findByIdAndQuestionAndDeleted(id, question, false));
    }

    public Optional<Answer> findByIdAndDeleted(long id, boolean deleted) {
        return Optional.ofNullable(repository.findByIdAndDeleted(id, deleted));
    }

    @Transactional
    public Answer create(Answer answer, Question question) {
        if (question == null || answer == null) {
            throw new IllegalArgumentException();
        }
        question.addAnswer(answer);
        return answer;
    }

    @Transactional
    public Answer update(long id, Answer updateAnswer) {
        Answer answer = findByIdAndDeleted(id, false).orElseThrow(AnswerNotFoundException::new);
        return answer.update(updateAnswer);
    }

    @Transactional
    public void delete(User loginUser, long id) {
        Answer answer = findByIdAndDeleted(id, false).orElseThrow(AnswerNotFoundException::new);
        answer.delete(loginUser);
    }

}
