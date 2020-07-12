package woos.bookassist.api;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.nio.charset.Charset;
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
                String.format("%s?query=%s", "/search/book", query), HttpMethod.GET,
                new HttpEntity(createHeaders(username, password)), new ParameterizedTypeReference<List<Document>>() {
                });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Document> body = responseEntity.getBody();
        assertThat(body.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testUnauthorized() {
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(
                "/search/book?query=java", HttpMethod.GET,
                null, Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testResourceNotFound() {
        String query = "notexistingbookquery";
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(
                String.format("%s?query=%s", "/search/book", query), HttpMethod.GET,
                new HttpEntity(createHeaders(username, password)), Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ref. - https://www.baeldung.com/how-to-use-resttemplate-with-basic-authentication-in-spring#manual_auth
    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

}