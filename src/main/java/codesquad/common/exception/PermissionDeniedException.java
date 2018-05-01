package codesquad.common.exception;

import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends CustomException {

    private static final String DEFAULT_MESSAGE = "권한이 없습니다";

    public PermissionDeniedException() {
        super(DEFAULT_MESSAGE);
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Object... arguments) {
        super(message, arguments);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}
