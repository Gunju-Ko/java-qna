package codesquad.common.exception;

import org.springframework.http.HttpStatus;

public class InternalServerError extends CustomException {

    public InternalServerError(String message) {
        super(message);
    }

    public InternalServerError(String message, Object... arguments) {
        super(message, arguments);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
