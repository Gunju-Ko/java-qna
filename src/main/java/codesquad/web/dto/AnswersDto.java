package codesquad.web.dto;

import codesquad.domain.Answer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class AnswersDto {

    private List<AnswerDto> contents;

    private Links links;

    public AnswersDto() {
    }

    public AnswersDto(List<AnswerDto> contents, Links links) {
        this.contents = contents;
        this.links = links;
    }

    public static AnswersDto of(Page<Answer> answers, String url) {
        return new AnswersDto(makeContents(answers), makeLinks(answers, url));
    }

    private static Links makeLinks(Page<Answer> answers, String url) {
        return Links.builder()
                    .url(url)
                    .page(answers)
                    .build();
    }

    private static List<AnswerDto> makeContents(Page<Answer> answers) {
        return answers.getContent()
                      .stream()
                      .map(Answer::toAnswerDto)
                      .collect(Collectors.toList());
    }

    @JsonIgnore
    public Link getPrevLink() {
        return links.getPrev();
    }

    @JsonIgnore
    public Link getNextLink() {
        return links.getNext();
    }

    @JsonIgnore
    public boolean isHasNext() {
        return links.getNext() != null;
    }

    @JsonIgnore
    public boolean isHasPrev() {
        return links.getPrev() != null;
    }

    @JsonIgnore
    public int getSize() {
        return contents.size();
    }
}
