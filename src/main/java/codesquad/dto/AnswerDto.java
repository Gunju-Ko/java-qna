package codesquad.dto;

import codesquad.domain.Answer;

import javax.validation.constraints.Size;

public class AnswerDto {

    private long id;

    @Size(min = 5)
    private String contents;

    private UserDto userDto;

    public AnswerDto() {
    }

    public AnswerDto(long id, String contents, UserDto userDto) {
        this.id = id;
        this.contents = contents;
        this.userDto = userDto;
    }

    public AnswerDto(String contents) {
        this.contents = contents;
    }

    public long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public Answer toAnswer() {
        return null;
    }
}
