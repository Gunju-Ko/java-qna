package codesquad.web.dto;

import codesquad.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UpdateUserDto {

    @Size(min = 3, max = 20)
    private String userId;

    @Size(min = 4, max = 20)
    private String password;

    @Size(min = 3, max = 20)
    private String name;

    @Email
    private String email;

    private String updatePassword;

    private String confirmUpdatePassword;

    public UpdateUserDto() {
    }

    public boolean isValidUpdatePassword() {
        if (StringUtils.isEmpty(updatePassword) || StringUtils.isEmpty(confirmUpdatePassword)) {
            return false;
        }
        if (updatePassword.length() < 4 || updatePassword.length() > 20) {
            return false;
        }
        return updatePassword.equals(confirmUpdatePassword);
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

