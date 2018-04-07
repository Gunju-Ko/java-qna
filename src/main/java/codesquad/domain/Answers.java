package codesquad.domain;

import codesquad.common.exception.CannotDeleteException;
import org.hibernate.annotations.Where;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<Answer> answers = new ArrayList<>();

    int getCountOfAnswers() {
        return answers.size();
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    void addAnswer(Answer answer) {
        answers.add(answer);
    }

    List<DeleteHistory> deleteAll(User loginUser) {
        if (!canDeleteAllAnswers(loginUser)) {
            throw new CannotDeleteException("해당 질문들을 삭제할 수 없습니다");
        }
        List<DeleteHistory> histories = answers.stream()
                                               .map(a -> a.delete(loginUser))
                                               .collect(Collectors.toList());
        answers.removeIf(Answer::isDeleted);
        return histories;
    }

    boolean canDeleteAllAnswers(User loginUser) {
        if (CollectionUtils.isEmpty(answers)) {
            return true;
        }
        return answers.stream()
                      .allMatch(a -> a.isOwner(loginUser));
    }
}
