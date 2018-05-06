package codesquad.web.dto;

import codesquad.domain.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class QuestionsDto {

    private List<QuestionDto> contents;

    private Links links;

    public QuestionsDto() {
    }

    public QuestionsDto(List<QuestionDto> contents, Links links) {
        this.contents = Objects.requireNonNull(contents);
        this.links = links;
    }

    public static QuestionsDto of(Page<Question> questions, String url) {
        return new QuestionsDto(makeContents(questions), makeLinks(questions, url));
    }

    public boolean isHasNext() {
        return links.getNext() != null;
    }

    public boolean isHasPrev() {
        return links.getPrev() != null;
    }

    @JsonIgnore
    public int getSize() {
        return contents.size();
    }

    @JsonIgnore
    public Link getNextLink() {
        return this.links.getNext();
    }

    private static Links makeLinks(Page<Question> questions, String url) {
        return Links.builder()
                    .url(url)
                    .page(questions)
                    .build();
    }

    @JsonIgnore
    public Link getPrevLink() {
        return this.links.getPrev();
    }

    private static List<QuestionDto> makeContents(Page<Question> questions) {
        return questions.getContent()
                        .stream()
                        .map(Question::toQuestionDto)
                        .collect(Collectors.toList());
    }
}
