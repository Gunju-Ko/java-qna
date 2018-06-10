package codesquad.domain;

import codesquad.common.exception.CannotDeleteException;
import org.junit.Before;
import org.junit.Test;
import support.UserTestMother;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {

    private User javajigi;

    private User gunju;

    private Answers answers;

    @Before
    public void setUp() throws Exception {
        javajigi = UserTestMother.javajigi();
        gunju = UserTestMother.gunju();

        answers = new Answers();
        Answer answer = Answer.builder()
                              .writer(javajigi)
                              .contents("테스트 답변 1")
                              .build();
        answers.addAnswer(answer);
    }

    @Test
    public void canDeleteAnswers_질문이존재하지않는경우() throws Exception {
        Answers answers = new Answers();
        assertThat(answers.canDeleteAllAnswers(javajigi)).isTrue();
    }

    @Test
    public void canDeleteAnswers_로그인한사람이답변의글쓴이인경우() throws Exception {
        assertThat(answers.canDeleteAllAnswers(javajigi)).isTrue();
    }

    @Test
    public void canDeleteAnswers_로그인한사람이답변의글쓴이가아닌경우() throws Exception {
        Answer answer = Answer.builder()
                              .writer(gunju)
                              .contents("테스트").build();

        answers.addAnswer(answer);
        assertThat(answers.canDeleteAllAnswers(javajigi)).isFalse();
    }

    @Test
    public void deleteAll_질문이존재하지않는경우() throws Exception {
        Answers answers = new Answers();
        List<DeleteHistory> histories = answers.deleteAll(javajigi);

        assertThat(histories.isEmpty()).isTrue();
    }

    @Test
    public void deleteAll_로그인한사람이답변의글쓴이인경우() throws Exception {
        List<DeleteHistory> histories = answers.deleteAll(javajigi);

        assertThat(histories.size()).isEqualTo(1);
        assertThat(answers.getCountOfAnswers()).isEqualTo(0);
    }

    @Test(expected = CannotDeleteException.class)
    public void deleteAll_로그인한사람이답변의글쓴이가아닌경우() throws Exception {
        // given
        Answer answer = Answer.builder()
                              .writer(gunju)
                              .contents("테스트")
                              .build();
        answers.addAnswer(answer);

        // when
        answers.deleteAll(javajigi);
    }

}