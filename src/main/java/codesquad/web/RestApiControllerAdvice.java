package codesquad.web;

import codesquad.CustomException;
import codesquad.UnAuthorizedException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "codesquad.web",
                      annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestApiControllerAdvice {

    private final MessageSourceAccessor accessor;

    public RestApiControllerAdvice(MessageSourceAccessor accessor) {
        this.accessor = accessor;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.status())
                             .body(new ErrorResponse(e.status(), getErrorMessage(e)));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUnAuthorizedException() {
        return new ErrorResponse(HttpStatus.FORBIDDEN, "해당 리소스에 대한 접근 권한이 없습니다");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(RuntimeException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private String getErrorMessage(CustomException e) {
        return accessor.getMessage(e.getMessageCode(), e.getArguments(), e.getMessage());
    }
}
