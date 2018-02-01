package codesquad.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Answer findByIdAndQuestionAndDeleted(long id, Question question, boolean deleted);

    Answer findByIdAndDeleted(long id, boolean deleted);

}
