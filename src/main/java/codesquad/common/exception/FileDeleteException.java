package codesquad.common.exception;

public class FileDeleteException extends InternalServerError {

    private static final String EXCEPTION_MESSAGE = "파일 삭제 실패";

    public FileDeleteException() {
        super(EXCEPTION_MESSAGE);
    }

    public FileDeleteException(String path) {
        super(EXCEPTION_MESSAGE, path);
    }
}
