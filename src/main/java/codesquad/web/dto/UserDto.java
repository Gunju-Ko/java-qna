package codesquad.web.dto;

import codesquad.domain.User;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@EqualsAndHashCode
public class UserDto {
    @Size(min = 3, max = 20)
    private String userId;

    @Size(min = 6, max = 20)
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

    public String getUserId() {
        return userId;
    }

    public UserDto setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public User toUser() {
        return new User(this.userId, this.password, this.name, this.email);
    }

    @Override
    public String toString() {
        return "UserDto [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
