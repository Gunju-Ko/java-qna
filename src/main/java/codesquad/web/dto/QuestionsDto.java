package codesquad.web.dto;

import codesquad.domain.Question;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class QuestionsDto {
    private List<QuestionDto> contents;

    public QuestionsDto() {
    }

    public QuestionsDto(List<QuestionDto> contents) {
        this.contents = Objects.requireNonNull(contents);
    }

    public int getSize() {
        return contents.size();
    }

    public static QuestionsDto of(List<Question> questions) {
        List<QuestionDto> contents = questions.stream()
                                              .map(Question::toQuestionDto)
                                              .collect(Collectors.toList());
        return new QuestionsDto(contents);
    }
}
