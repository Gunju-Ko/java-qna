package codesquad.domain;

import codesquad.UnAuthorizedException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {

    private Question question;

    private User defaultUser;

    @Before
    public void setUp() throws Exception {
        defaultUser = new User(1, "javajigi", "test", "자바지기", "javajigi@slipp.net");
        question = new Question("test", "content");
        question.writeBy(defaultUser);
    }

    @Test
    public void delete() throws Exception {
        question.delete(defaultUser);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_권한이없는유저() throws Exception {
        User user = new User(3, "gunju", "test", "고건주", "gunju@slipp.net");
        question.delete(user);
    }

    @Test
    public void isOwner() throws Exception {
        assertThat(question.isOwner(defaultUser)).isTrue();
        assertThat(question.isOwner(new User(3, "gunju", "test", "고건주", "gunju@slipp.net"))).isFalse();
    }

    @Test
    public void update() throws Exception {
        Question updatedQuestion = new Question("update", "update test");
        question.update(defaultUser, updatedQuestion);
        assertThat(question.getTitle()).isEqualTo(updatedQuestion.getTitle());
        assertThat(question.getContents()).isEqualTo(updatedQuestion.getContents());
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_권한이없는유저() throws Exception {
        User user = new User(3, "gunju", "test", "고건주", "gunju@slipp.net");
        question.update(user, new Question("test", "test"));
    }

}