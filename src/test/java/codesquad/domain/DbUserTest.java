package codesquad.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbUserTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void photo() throws Exception {
        User javajigi = repository.findOne(1L);
        User gunju = repository.findOne(3L);

        assertThat(javajigi).isNotNull();
        assertThat(gunju).isNotNull();

        assertThat(javajigi.getPhoto()).isEqualTo("/images/default_image.png");
        assertThat(gunju.getPhoto()).isEqualTo("/images/users/gunju.jpeg");
    }
}
