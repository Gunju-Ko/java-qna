package codesquad.common.exception;

public class InvalidPasswordException extends PermissionDeniedException {

    private static final String DEFAULT_MESSAGE = "잘못된 패스워드 입니다";

    public InvalidPasswordException() {
        super(DEFAULT_MESSAGE);
    }

}
