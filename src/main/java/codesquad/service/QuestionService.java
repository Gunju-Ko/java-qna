package codesquad.service;

import codesquad.QuestionNotFoundException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service("qnaService")
public class QuestionService {
    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    public Question create(User loginUser, Question question) {
        question.writeBy(loginUser);
        log.debug("question : {}", question);
        return questionRepository.save(question);
    }

    public Optional<Question> findById(long id) {
        return Optional.ofNullable(questionRepository.findOne(id));
    }

    public Optional<Question> findByIdAndNotDeleted(long id) {
        Question question = questionRepository.findOne(id);
        if (question == null || question.isDeleted()) {
            return Optional.empty();
        }
        return Optional.of(question);
    }

    @Transactional
    public Question update(User loginUser, long id, Question updatedQuestion) {
        Optional<Question> question = findByIdAndNotDeleted(id);

        return question.orElseThrow(() -> new QuestionNotFoundException(id))
                       .update(loginUser, updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(User loginUser, long id) {
        Optional<Question> question = findByIdAndNotDeleted(id);

        question.orElseThrow(() -> new QuestionNotFoundException(id))
                .delete(loginUser);
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public boolean isOwnerOfQuestion(User loginUser, long id) {
        Question question = questionRepository.findOne(id);
        if (question == null) {
            throw new QuestionNotFoundException(id);
        }
        return question.isOwner(loginUser);
    }
}
