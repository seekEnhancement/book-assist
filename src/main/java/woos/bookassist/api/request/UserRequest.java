package woos.bookassist.api.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class UserRequest {
    @NotEmpty
    @Size(min = 2, max = 12)
    String id;

    @NotEmpty
    @Size(min = 2, max = 20)
    String name;

    @NotEmpty
    @Size(min = 2, max = 20)
    String password;

    @NotEmpty
    @Size(min = 2, max = 20)
    String passwordVerify;

    @Email
    String email;

    @AssertTrue(message = "passwordVerify should be equal to password.")
    private boolean isPasswordConfirm() {
        return this.password.equals(this.passwordVerify);
    }
}
