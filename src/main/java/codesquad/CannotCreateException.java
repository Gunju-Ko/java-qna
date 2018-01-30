package codesquad;

public class CannotCreateException extends InternalServerError {
    public CannotCreateException() {
        super("리소스 생성 실패");
    }

    public CannotCreateException(String message) {
        super(message);
    }
}
