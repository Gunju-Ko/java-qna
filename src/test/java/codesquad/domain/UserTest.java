package codesquad.domain;

import codesquad.common.exception.InvalidPasswordException;
import codesquad.common.exception.PermissionDeniedException;
import org.junit.Before;
import org.junit.Test;
import support.UserTestMother;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserTest {

    private User target;

    @Before
    public void setUp() throws Exception {
        target = User.builder()
                     .userId("sanjigi")
                     .password("test")
                     .name("산지기2")
                     .email("sanjigi2@slipp.net")
                     .build();
    }

    @Test
    public void update_owner() throws Exception {
        User origin = UserTestMother.sanjigi();
        User loginUser = origin;

        origin.update(loginUser, target, null);
        assertThat(origin.getName(), is(target.getName()));
        assertThat(origin.getEmail(), is(target.getEmail()));
    }

    @Test(expected = PermissionDeniedException.class)
    public void update_not_owner() throws Exception {
        User origin = UserTestMother.sanjigi();
        User loginUser = UserTestMother.javajigi();
        origin.update(loginUser, target, null);
    }

    @Test(expected = InvalidPasswordException.class)
    public void update_mismatch_password() {
        User origin = UserTestMother.sanjigi();
        User loginUser = origin;

        target = User.builder()
                     .userId("sanjigi")
                     .password("test2") // wrong password
                     .name("산지기2")
                     .email("sanjigi2@slipp.net")
                     .build();

        origin.update(loginUser, target, null);
    }
}
