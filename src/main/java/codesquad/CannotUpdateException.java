package codesquad;

public class CannotUpdateException extends InternalServerError {

    public CannotUpdateException() {
        super("리소스 업데이트 실패");
    }

    public CannotUpdateException(String message) {
        super(message);
    }
}
