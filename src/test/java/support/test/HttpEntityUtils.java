package support.test;

import codesquad.domain.User;
import codesquad.web.dto.AnswerDto;
import codesquad.web.dto.QuestionDto;
import codesquad.web.dto.UpdateUserDto;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

import static support.test.HtmlFormDataBuilder.formUrlEncodedForm;

public class HttpEntityUtils {

    private HttpEntityUtils() {}

    public static HttpEntity<MultiValueMap<String, Object>> makeFormUrlEncodedRequest(UpdateUserDto user) {
        return formUrlEncodedForm().addParameter("_method", "put")
                                   .addParameter("password", user.getPassword())
                                   .addParameter("name", user.getName())
                                   .addParameter("email", user.getEmail())
                                   .addParameter("updatePassword", user.getUpdatePassword())
                                   .addParameter("confirmUpdatePassword", user.getConfirmUpdatePassword()).build();

    }

    public static HttpEntity<MultiValueMap<String, Object>> makeFormUrlEncodedRequest(User user) {
        return formUrlEncodedForm().addParameter("userId", user.getUserId())
                                   .addParameter("password", user.getPassword())
                                   .addParameter("name", user.getName())
                                   .addParameter("email", user.getEmail())
                                   .build();
    }

    public static HttpEntity<MultiValueMap<String, Object>> makeFormUrlEncodedRequest(QuestionDto question) {
        return formUrlEncodedForm().addParameter("title", question.getTitle())
                                   .addParameter("contents", question.getContents())
                                   .build();
    }

    public static HttpEntity<MultiValueMap<String, Object>> makeFormUrlEncodedRequest(AnswerDto question) {
        return formUrlEncodedForm().addParameter("contents", question.getContents())
                                   .build();
    }
}
