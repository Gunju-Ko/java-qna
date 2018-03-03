package codesquad;

public class UserNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "해당 유저를 찾을수 없습니다.";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(long id) {
        super(DEFAULT_MESSAGE, id);
    }

}
