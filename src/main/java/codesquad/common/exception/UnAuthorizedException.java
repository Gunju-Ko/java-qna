package codesquad.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomException {

    private static final String DEFAULT_MESSAGE = "로그인이 필요 합니다";

    public UnAuthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UnAuthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
