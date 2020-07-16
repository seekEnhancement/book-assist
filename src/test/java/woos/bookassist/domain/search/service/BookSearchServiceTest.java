package woos.bookassist.domain.search.service;

import org.awaitility.core.ThrowingRunnable;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import woos.bookassist.domain.search.repository.QueryRecommend;
import woos.bookassist.util.AwaitUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
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
        AwaitUtils.awaitAndCheck(100, 200, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                var userSearches = bookSearchService.getUserSearches(userId);
                assertThat(userSearches.size()).isGreaterThanOrEqualTo(1);
            }
        });
    }

    @Order(3)
    @Test
    public void getTop10Queries() {
        AwaitUtils.awaitAndCheck(1000, 2000, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                // first call - cache make
                var top10Queries = bookSearchService.getTop10Queries();
                assertThat(top10Queries.size()).isGreaterThanOrEqualTo(1);

                // more searches before caffeine cache expiring
                String query2 = "Spring Boot";
                bookSearchService.search(userId, query2, 1, 10);
                String query3 = "Spring Cloud";
                bookSearchService.search(userId, query3, 1, 10);
            }
        });

        // cached top10 queries does not contains new searches
        AwaitUtils.awaitAndCheck(2000, 3000, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                var top10Queries2 = bookSearchService.getTop10Queries();
                assertThat(getQueriesOnly(top10Queries2)).doesNotContain("Spring Boot", "Spring Cloud");
            }
        });

        // after cache expire - cache re-synced
        AwaitUtils.awaitAndCheck(11000, 12000, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                var top10Queries3 = bookSearchService.getTop10Queries();
                assertThat(getQueriesOnly(top10Queries3)).contains("Spring Boot", "Spring Cloud");
            }
        });
    }

    private List<String> getQueriesOnly(List<QueryRecommend> top10Queries) {
        return top10Queries.stream().map(QueryRecommend::getQuery).collect(Collectors.toList());
    }
}