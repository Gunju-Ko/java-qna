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

    public static QuestionsDto of(Page<Question> questions) {
        return new QuestionsDto(makeContents(questions), makeLinks(questions));
    }

    private static List<QuestionDto> makeContents(Page<Question> questions) {
        return questions.getContent()
                        .stream()
                        .map(Question::toQuestionDto)
                        .collect(Collectors.toList());
    }

    private static Links makeLinks(Page<Question> questions) {
        return Links.builder()
                    .url("/api/questions")
                    .page(questions)
                    .build();
    }

    @JsonIgnore
    public int getSize() {
        return contents.size();
    }

    @JsonIgnore
    public Link getNextLink() {
        return this.links.getNext();
    }

    @JsonIgnore
    public Link getPrevLink() {
        return this.links.getPrev();
    }
}
