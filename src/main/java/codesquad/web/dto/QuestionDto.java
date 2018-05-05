package codesquad.web.dto;

import codesquad.domain.Question;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class QuestionDto {
    private long id;

    @Size(min = 3, max = 100)
    private String title;

    @Size(min = 3)
    private String contents;

    private UserDto writer;

    public QuestionDto() {
    }

    public QuestionDto(String title, String contents) {
        this(0, title, contents);
    }

    public QuestionDto(long id, String title, String contents) {
        this(id, title, contents, null);
    }

    public QuestionDto(long id, String title, String contents, UserDto writer) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
    }

    public Question toQuestion() {
        return new Question(this.title, this.contents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionDto)) {
            return false;
        }

        QuestionDto that = (QuestionDto) o;

        if (title != null ? !title.equals(that.title) : that.title != null) {
            return false;
        }
        return contents != null ? contents.equals(that.contents) : that.contents == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (contents != null ? contents.hashCode() : 0);
        return result;
    }
}
