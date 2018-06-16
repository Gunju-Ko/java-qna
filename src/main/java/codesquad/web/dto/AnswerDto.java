package codesquad.web.dto;

import codesquad.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class AnswerDto {

    private long id;

    private UserDto writer;

    @Size(min = 5)
    private String contents;

    private boolean deleted;

    private String formattedCreateDate;

    public AnswerDto() {
    }

    public AnswerDto(String contents) {
        this.contents = contents;
    }

    public Answer toAnswer() {
        Answer.AnswerBuilder builder = Answer.builder();

        builder.contents(this.contents)
               .deleted(this.deleted);

        if (this.writer != null) {
            builder.writer(this.writer.toUser());
        }
        return builder.build();
    }
}
