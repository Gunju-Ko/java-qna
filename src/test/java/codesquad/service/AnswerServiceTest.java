package codesquad.service;

import codesquad.UnAuthorizedException;
import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AnswerServiceTest {

    private User javajigi;

    private User gunju;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Before
    public void setUp() throws Exception {
        javajigi = new User(1, "javajigi", "test", "자바지기", "javajigi@slipp.net");
        gunju = new User(2, "gunju", "test", "건주", "test@email.com");
    }

    @Test
    public void create() throws Exception {
        Question question = questionRepository.findOne(1L);
        Answer answer = new Answer(javajigi, "test contents");
        answerService.create(answer, question);

        assertThat(question.getAnswers().size()).isEqualTo(3);
    }

    @Test
    public void update() throws Exception {
        Answer updateAnswer = new Answer(javajigi, "내용 업데이트");

        answerService.update(1L, updateAnswer);
        Answer dbAnswer = answerRepository.findOne(1L);
        assertThat(dbAnswer.getContents()).isEqualTo("내용 업데이트");
    }

    @Test
    public void delete() throws Exception {
        answerService.delete(javajigi, 1);
        Answer answer = answerRepository.findByIdAndDeleted(1L, false);

        Question dbQuestion = questionRepository.findOne(1L);
        assertThat(answer).isNull();
        assertThat(dbQuestion.getAnswers().size()).isEqualTo(1);
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_권한이없는사용자() throws Exception {
        answerService.delete(gunju, 1);
    }

    @Test
    public void isOwnerOfAnswer() throws Exception {
        assertThat(answerService.isOwnerOfAnswer(javajigi, 1L)).isTrue();
        assertThat(answerService.isOwnerOfAnswer(gunju, 1L)).isFalse();
    }

}