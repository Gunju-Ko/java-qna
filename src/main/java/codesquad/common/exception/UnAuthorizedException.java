package codesquad.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomException {

    private static final String DEFAULT_MESSAGE = "권한이 없습니다";

    public UnAuthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(String message, Object... arguments) {
        super(message, arguments);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}
