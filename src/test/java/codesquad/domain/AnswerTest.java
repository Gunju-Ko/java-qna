package codesquad.domain;

import codesquad.common.exception.PermissionDeniedException;
import org.junit.Before;
import org.junit.Test;
import support.UserTestMother;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {

    private Answer answer;

    private User javajigi;

    private User gunju;

    @Before
    public void setUp() throws Exception {
        javajigi = UserTestMother.javajigi();
        gunju = UserTestMother.gunju();

        answer = Answer.builder()
                       .writer(javajigi)
                       .contents("test contents")
                       .build();
    }

    @Test
    public void isOwner() throws Exception {
        assertThat(answer.isOwner(javajigi)).isTrue();
        assertThat(answer.isOwner(gunju)).isFalse();
    }

    @Test
    public void update() throws Exception {
        Answer updateAnswer = Answer.builder()
                                    .writer(javajigi)
                                    .contents("update test")
                                    .build();
        answer.update(updateAnswer);

        assertThat(answer.getContents()).isEqualTo("update test");
    }

    @Test(expected = PermissionDeniedException.class)
    public void update_권한이없는사용자() throws Exception {
        Answer updateAnswer = Answer.builder()
                                    .writer(gunju)
                                    .contents("update test")
                                    .build();

        answer.update(updateAnswer);
    }

    @Test
    public void delete() throws Exception {
        answer.delete(javajigi);

        assertThat(answer.isDeleted()).isTrue();
    }

    @Test(expected = PermissionDeniedException.class)
    public void delete_권한이없는사용자() throws Exception {
        answer.delete(gunju);
    }

}