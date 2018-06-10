package codesquad.domain;

import codesquad.common.exception.PermissionDeniedException;
import codesquad.web.dto.AnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import support.domain.AbstractEntity;
import support.domain.ApiUrlGeneratable;
import support.domain.UrlGeneratable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.net.URI;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Entity
public class Answer extends AbstractEntity implements UrlGeneratable, ApiUrlGeneratable {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Size(min = 5)
    @Lob
    private String contents;

    private boolean deleted = false;

    public Answer() {
    }

    @Override
    public String generateUrl() {
        return String.format("%s/answers/%d", question.generateUrl(), getId());
    }

    @Override
    public URI generateApiUri() {
        String apiUri = "/api" + generateUrl();
        return URI.create(apiUri);
    }

    public Answer update(Answer updateAnswer) {
        if (!isOwner(updateAnswer.writer)) {
            throw new PermissionDeniedException();
        }
        this.contents = updateAnswer.contents;
        return this;
    }

    public DeleteHistory delete(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new PermissionDeniedException();
        }
        this.deleted = true;
        return new DeleteHistory(ContentType.ANSWER, getId(), loginUser, LocalDateTime.now());
    }

    public void checkAuthority(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new PermissionDeniedException();
        }
    }

    public boolean isOwner(User loginUser) {
        return writer.equals(loginUser);
    }

    public void writerBy(User writer) {
        if (this.writer != null && !this.writer.equals(writer)) {
            throw new IllegalStateException("Can not change writer");
        }
        this.writer = writer;
    }

    public AnswerDto toAnswerDto() {
        AnswerDto.AnswerDtoBuilder builder = AnswerDto.builder()
                                                      .id(getId())
                                                      .contents(this.contents)
                                                      .deleted(this.isDeleted());
        if (this.writer != null) {
            builder.writer(this.writer.toUserDto());
        }
        return builder.build();
    }

    public User getWriter() {
        return writer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        if (this.question != null && !this.question.equals(question)) {
            throw new IllegalStateException("This answer already has a question");
        }
        this.question = question;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "Answer [id=" + getId() + ", writer=" + writer + ", contents=" + contents + "]";
    }
}
