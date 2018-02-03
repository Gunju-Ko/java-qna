package codesquad.domain;

import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<Answer> answers = new ArrayList<>();

    int getCountOfAnswers() {
        return answers.size();
    }

    void addAnswer(Answer answer) {
        answers.add(answer);
    }

    void deleteAnswer(Answer answer) {
        if (!answers.remove(answer)) {
            throw new IllegalStateException();
        }
    }
}
