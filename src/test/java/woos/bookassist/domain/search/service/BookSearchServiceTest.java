package woos.bookassist.domain.search.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import woos.bookassist.domain.search.repository.QueryRecommend;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(value = {"spring.cache.caffeine.spec: expireAfterWrite=1s"})
@DirtiesContext
class BookSearchServiceTest {
    @Autowired
    BookSearchService bookSearchService;

    @Value("${spring.security.user.username")
    String userId;

    @Order(1)
    @Test
    public void testSearch() {
        String query = "java";
        var result = bookSearchService.search(userId, query, 1, 10);
        assertThat(result).isNotNull();
        assertThat(result.getDocuments()).isNotNull();
        assertThat(result.getDocuments().size()).isGreaterThanOrEqualTo(1);
    }

    @Order(2)
    @Test
    public void testGetUserSearches() {
        var userSearches = bookSearchService.getUserSearches(userId);
        assertThat(userSearches.size()).isGreaterThanOrEqualTo(1);
    }

    @Order(3)
    @Test
    public void getTop10Queries() {
        var top10Queries = bookSearchService.getTop10Queries();
        assertThat(top10Queries.size()).isGreaterThanOrEqualTo(1);

        // more searches before caffeine cache expiring
        String query2 = "김미경의 리부트";
        bookSearchService.search(userId, query2, 1, 10);
        String query3 = "부자";
        bookSearchService.search(userId, query3, 1, 10);

        // cached top10 queries does not contains new searches
        var top10Queries2 = bookSearchService.getTop10Queries();
        assertThat(getQueriesOnly(top10Queries2)).doesNotContain("김미경의 리부트", "부자");

        // after cache expire - overridden with spring.cache.caffeine.spec: expireAfterWrite=1s
        // cache re-synced
        await()
                .pollDelay(Duration.ofSeconds(1))
                .atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var top10Queries3 = bookSearchService.getTop10Queries();
                    assertThat(getQueriesOnly(top10Queries3)).contains("김미경의 리부트", "부자");
                });

    }

    private List<String> getQueriesOnly(List<QueryRecommend> top10Queries) {
        return top10Queries.stream().map(QueryRecommend::getQuery).collect(Collectors.toList());
    }
}