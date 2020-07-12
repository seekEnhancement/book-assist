package woos.bookassist.remote.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NaverSearchClientTest {
    @Autowired
    NaverSearchClient naverSearchClient;

    @Test
    public void testSearchNormal() {
        String query = "미움받을 용기";

        // start 1
        NaverSearchResult naverSearchResult = naverSearchClient.search(query, 1, 10);
        assertThat(naverSearchResult.getItems()).isNotNull();
        assertThat(naverSearchResult.getItems().size()).isGreaterThan(1);
        NaverSearchResult.Item item1 = naverSearchResult.getItems().get(0);
        assertThat(item1.getTitle()).contains(query);

        // start 11
        if (naverSearchResult.getTotal() > 10) {
            NaverSearchResult naverSearchResult2 = naverSearchClient.search(query, 11, 10);
            assertThat(naverSearchResult2.getItems().get(0).getTitle()).contains(query);
        }
    }
}