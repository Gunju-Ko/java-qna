package codesquad.common.exception;

public class CannotDeleteException extends UnAuthorizedException {

    private static final String DEFAULT_MESSAGE = "해당 질문은 삭제할 수 없습니다";

    public CannotDeleteException() {
        super(DEFAULT_MESSAGE);
    }

    public CannotDeleteException(String message) {
        super(message);
    }
}