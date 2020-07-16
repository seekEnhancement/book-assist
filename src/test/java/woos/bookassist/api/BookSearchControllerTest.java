package woos.bookassist.api;

import org.awaitility.core.ThrowingRunnable;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;
import woos.bookassist.api.v1.QueryRecommendV1;
import woos.bookassist.util.AwaitUtils;
import woos.bookassist.util.ControllerUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static woos.bookassist.remote.openapi.BookSearchResult.Document;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookSearchControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Value("${spring.security.user.name}")
    String username;

    @Value("${spring.security.user.password}")
    String password;

    @Test
    public void testSearchBook() {
        String query = "java";
        var responseEntity = testRestTemplate.exchange(
                String.format("%s?query=%s", "/book/search", query), HttpMethod.GET,
                new HttpEntity(ControllerUtils.createHeaders(username, password)),
                new ParameterizedTypeReference<List<Document>>() {
                });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Document> body = responseEntity.getBody();
        assertThat(body.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testUnauthorized() {
        var responseEntity = testRestTemplate.exchange(
                "/book/search?query=java", HttpMethod.GET,
                null, Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testResourceNotFound() {
        String query = "notexistingbookquery";
        var responseEntity = testRestTemplate.exchange(
                String.format("%s?query=%s", "/book/search", query), HttpMethod.GET,
                new HttpEntity(ControllerUtils.createHeaders(username, password)), Object.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Order(1)
    @ParameterizedTest
    @ValueSource(strings = {"김미경의 리부트", "돈의 속성", "더 해빙", "흔한 남매", "설민석의 한국사 대모험",
            "이제부터는 오를 곳만 오른다", "하고 싶은 대로 살아도 괜찮아", "큰별쌤 최태성", "동아 초등 새국어사전", "코로나 이후의 세계"})
    public void testManySearches(String query) {
        String bookSearchUrl = UriComponentsBuilder.fromPath("/book/search").queryParam("query", query)
                .build(false).toUriString();

        var responseEntity = testRestTemplate.exchange(
                bookSearchUrl, HttpMethod.GET,
                new HttpEntity(ControllerUtils.createHeaders(username, password)),
                new ParameterizedTypeReference<List<Document>>() {
                });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Document> body = responseEntity.getBody();
        assertThat(body.size()).isGreaterThanOrEqualTo(1);
    }

    @Order(2)
    @Test
    public void testRecommend() {
        AwaitUtils.awaitAndCheck(2000, 3000, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                var responseEntity = testRestTemplate.exchange("/book/recommend",
                        HttpMethod.GET, new HttpEntity(ControllerUtils.createHeaders(username, password)),
                        new ParameterizedTypeReference<List<QueryRecommendV1>>() {
                        });
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                var body = responseEntity.getBody();
                assertThat(body.size()).isEqualTo(10);
            }
        });
    }

}