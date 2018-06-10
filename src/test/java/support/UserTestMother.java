package support;

import codesquad.domain.User;

public class UserTestMother {

    public static User javajigi() {
        return new User(1L, "javajigi", "test", "자바지기", "javajigi@slipp.net");
    }

    public static User sanjigi() {
        return new User(2L, "sanjigi", "test", "산지기", "sanjigi@slipp.net");
    }

    public static User gunju() {
        return new User(3L, "gunju", "test", "건주", "gunju@slipp.net");
    }

}
