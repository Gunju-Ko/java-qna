package codesquad.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Iterable<Question> findByDeleted(boolean deleted);

    Page<Question> findByDeleted(boolean deleted, Pageable pageable);

    List<Question> findByWriter(User user);
}
