package woos.bookassist.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import woos.bookassist.common.exception.ErrorResponse;
import woos.bookassist.common.exception.UserRegisterFailedException;
import woos.bookassist.util.ControllerUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void testRegisterUser() {
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/user/register", HttpMethod.POST,
                new HttpEntity(createUserRequestBody()), new ParameterizedTypeReference<Void>() {
                });
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        // db user book search
        canSearchWithRegisteredUser();
    }

    private void canSearchWithRegisteredUser() {
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(
                "/book/search?query=java", HttpMethod.GET,
                new HttpEntity(ControllerUtils.createHeaders("seek", "pwd00")), Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testRegisterUser_invalid() {
        ResponseEntity<ErrorResponse> responseEntity = testRestTemplate.exchange(
                "/user/register", HttpMethod.POST,
                new HttpEntity(createInvalidUserRequestBody()), new ParameterizedTypeReference<ErrorResponse>() {
                });
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getCode()).isEqualTo(UserRegisterFailedException.CODE_USER_REGISTER_FAILED);
        assertThat(responseEntity.getBody().getMessage()).contains("passwordVerify should be equal to password");
    }

    private Map<String, String> createUserRequestBody() {
        return Map.of("id", "seek", "name", "우병훈",
                "password", "pwd00", "passwordVerify", "pwd00", "email", "woos@woos.me");
    }

    private Map<String, String> createInvalidUserRequestBody() {
        return Map.of("id", "s", "name", "우",
                "password", "pwd00", "passwordVerify", "pwd01", "email", "invalidemail");
    }

}