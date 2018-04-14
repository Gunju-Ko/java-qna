package codesquad.web.dto;

import codesquad.domain.User;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Size(min = 3, max = 20)
    private String userId;

    @Size(min = 4, max = 20)
    private String password;

    @Size(min = 3, max = 20)
    private String name;

    @Email
    private String email;

    public UserDto() {
    }

    public UserDto(String userId, String password, String name, String email) {
        super();
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toUser() {
        return new User(this.userId, this.password, this.name, this.email);
    }
}
