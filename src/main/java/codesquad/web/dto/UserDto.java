package codesquad.web.dto;

import codesquad.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class UserDto {

    private long id;

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

    public User toUser() {
        return User.builder()
                   .userId(userId)
                   .password(password)
                   .name(name)
                   .email(email)
                   .build();
    }
}
