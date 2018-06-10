package codesquad.domain;

import codesquad.common.exception.CannotDeleteException;
import codesquad.common.exception.PermissionDeniedException;
import org.junit.Before;
import org.junit.Test;
import support.UserTestMother;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {

    private Question question;

    private Question updateQuestion;

    private User javajigi;

    private User gunju;


    @Before
    public void setUp() throws Exception {
        javajigi = UserTestMother.javajigi();
        gunju = UserTestMother.gunju();

        question = Question.builder()
                           .title("title")
                           .contents("contents")
                           .writer(javajigi)
                           .answers(new Answers())
                           .build();

        updateQuestion = Question.builder()
                                 .title("update title")
                                 .contents("update contents")
                                 .answers(new Answers())
                                 .build();

        Answer answer = Answer.builder()
                              .writer(javajigi)
                              .contents("테스트 답변")
                              .build();

        question.addAnswer(answer);
    }

    @Test
    public void delete() throws Exception {
        List<DeleteHistory> histories = question.delete(javajigi);
        assertThat(question.isDeleted()).isTrue();
        assertThat(histories.size()).isEqualTo(2);
        assertThat(question.getCountOfAnswers()).isEqualTo(0);
    }

    @Test(expected = PermissionDeniedException.class)
    public void delete_권한이없는유저() throws Exception {
        question.delete(gunju);
    }

    @Test
    public void isOwner() throws Exception {
        assertThat(question.isOwner(javajigi)).isTrue();
        assertThat(question.isOwner(gunju)).isFalse();
    }

    @Test(expected = PermissionDeniedException.class)
    public void update_권한이없는유저() throws Exception {
        question.update(gunju, updateQuestion);
    }

    @Test
    public void update() throws Exception {
        question.update(javajigi, updateQuestion);
        assertThat(compareTitleAndContents(question, updateQuestion)).isTrue();
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_로그인한유저가작성하지않은답변이있는경우() throws Exception {
        Answer answer = Answer.builder()
                              .writer(gunju)
                              .contents("테스트 답변2")
                              .build();
        question.addAnswer(answer);
        question.delete(javajigi);
    }

    private boolean compareTitleAndContents(Question o1, Question o2) {
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.getTitle().equals(o2.getTitle()) && o1.getContents().equals(o2.getContents());
    }

}