package woos.bookassist.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import woos.bookassist.util.ControllerUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static woos.bookassist.remote.openapi.BookSearchResult.Document;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookSearchControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Value("${spring.security.user.name}")
    String username;

    @Value("${spring.security.user.password}")
    String password;

    @Test
    public void testSearchBook() {
        String query = "java";
        ResponseEntity<List<Document>> responseEntity = testRestTemplate.exchange(
                String.format("%s?query=%s", "/book/search", query), HttpMethod.GET,
                new HttpEntity(ControllerUtils.createHeaders(username, password)), new ParameterizedTypeReference<List<Document>>() {
                });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Document> body = responseEntity.getBody();
        assertThat(body.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testUnauthorized() {
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(
                "/book/search?query=java", HttpMethod.GET,
                null, Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testResourceNotFound() {
        String query = "notexistingbookquery";
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(
                String.format("%s?query=%s", "/book/search", query), HttpMethod.GET,
                new HttpEntity(ControllerUtils.createHeaders(username, password)), Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}