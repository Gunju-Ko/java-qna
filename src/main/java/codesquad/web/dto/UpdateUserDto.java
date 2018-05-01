package codesquad.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateUserDto extends UserDto {

    private String updatePassword;

    private String confirmUpdatePassword;

    public UpdateUserDto() {
    }

    private UpdateUserDto(String userId,
                          String password,
                          String name,
                          String email,
                          String updatePassword,
                          String confirmUpdatePassword) {
        super(userId, password, name, email);
        this.updatePassword = updatePassword;
        this.confirmUpdatePassword = confirmUpdatePassword;
    }

    public static UpdateUserDtoBuilder builder() {
        return new UpdateUserDtoBuilder();
    }

    public boolean isValidUpdatePassword() {
        if (StringUtils.isEmpty(updatePassword)) {
            return StringUtils.isEmpty(confirmUpdatePassword);
        }
        if (updatePassword.length() < 4 || updatePassword.length() > 20) {
            return false;
        }
        return updatePassword.equals(confirmUpdatePassword);
    }

    public static final class UpdateUserDtoBuilder {
        private String password;
        private String name;
        private String email;
        private String userId;
        private String updatePassword;
        private String confirmUpdatePassword;

        private UpdateUserDtoBuilder() {}

        public UpdateUserDto build() {
            return new UpdateUserDto(userId, password, name, email, updatePassword, confirmUpdatePassword);
        }

        public UpdateUserDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UpdateUserDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UpdateUserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UpdateUserDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public UpdateUserDtoBuilder updatePassword(String updatePassword) {
            this.updatePassword = updatePassword;
            return this;
        }

        public UpdateUserDtoBuilder confirmUpdatePassword(String confirmUpdatePassword) {
            this.confirmUpdatePassword = confirmUpdatePassword;
            return this;
        }
    }
}
