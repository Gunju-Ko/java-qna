package codesquad.web.dto;

import codesquad.domain.Answers;
import codesquad.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@Data
public class QuestionDto {

    private long id;

    @Size(min = 3, max = 100)
    private String title;

    @Size(min = 3)
    private String contents;

    private UserDto writer;

    private boolean deleted = false;

    private int countOfAnswers;

    private String formattedCreateDate;

    private Paged<AnswerDto> answers;

    public QuestionDto() {
    }

    public Question toQuestion() {
        Question.QuestionBuilder builder = Question.builder();
        builder.title(this.title)
               .contents(this.contents)
               .answers(new Answers())
               .build();

        if (this.writer != null) {
            builder.writer(this.writer.toUser());
        }

        return builder.build();
    }
}
