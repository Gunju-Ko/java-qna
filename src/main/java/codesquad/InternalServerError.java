package codesquad;

import org.springframework.http.HttpStatus;

public class InternalServerError extends CustomException {

    public InternalServerError() {
    }

    public InternalServerError(String message) {
        super(message);
    }

    @Override
    HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
