package codesquad.domain;

import codesquad.common.exception.PermissionDeniedException;
import codesquad.web.dto.QuestionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import support.domain.AbstractEntity;
import support.domain.ApiUrlGeneratable;
import support.domain.UrlGeneratable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Entity
public class Question extends AbstractEntity implements UrlGeneratable, ApiUrlGeneratable {
    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false)
    private String title;

    @Size(min = 3)
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    private boolean deleted = false;

    public Question() {
    }

    @Override
    public String generateUrl() {
        return String.format("/questions/%d", getId());
    }

    @Override
    public URI generateApiUri() {
        String apiUri = "/api" + generateUrl();
        return URI.create(apiUri);
    }

    public void writeBy(User loginUser) {
        this.writer = loginUser;
    }

    public void addAnswer(Answer answer) {
        answer.setQuestion(this);
        answers.addAnswer(answer);
    }

    public List<DeleteHistory> delete(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new PermissionDeniedException("권한이없습니다");
        }
        List<DeleteHistory> histories = answers.deleteAll(loginUser);
        this.deleted = true;
        histories.add(new DeleteHistory(ContentType.QUESTION, getId(), loginUser, LocalDateTime.now()));

        return histories;
    }

    public Question update(User loginUser, Question updatedQuestion) {
        if (!isOwner(loginUser)) {
            throw new PermissionDeniedException();
        }
        this.title = updatedQuestion.title;
        this.contents = updatedQuestion.contents;
        return this;
    }

    public boolean isOwner(User loginUser) {
        if (this.writer == null) {
            throw new IllegalStateException();
        }
        return writer.equals(loginUser);
    }

    public void checkAuthority(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new PermissionDeniedException();
        }
    }

    public QuestionDto toQuestionDto() {
        QuestionDto.QuestionDtoBuilder builder = QuestionDto.builder();

        builder.id(this.getId())
               .title(this.title)
               .contents(this.contents)
               .deleted(this.deleted)
               .formattedCreateDate(this.getFormattedCreateDate())
               .countOfAnswers(getCountOfAnswers());

        if (this.writer != null) {
            builder.writer(this.writer.toUserDto());
        }

        return builder.build();
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public User getWriter() {
        return writer;
    }

    public List<Answer> getAnswers() {
        return answers.getAnswers();
    }

    public int getCountOfAnswers() {
        return answers.getCountOfAnswers();
    }

    @Override
    public String toString() {
        return "Question [id=" + getId() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }
}
