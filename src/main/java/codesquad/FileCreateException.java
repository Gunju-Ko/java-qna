package codesquad;

public class FileCreateException extends InternalServerError {

    private static final String ERROR_MESSAGE = "파일 생성중 오류가 발생했습니다";

    public FileCreateException() {
        super(ERROR_MESSAGE);
    }

    public FileCreateException(String message) {
        super(ERROR_MESSAGE, message);
    }

}
