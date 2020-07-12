package woos.bookassist.remote.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookSearchClientTest {

    @Autowired
    BookSearchClient bookSearchClient;

    @Test
    public void testSearchNormal() {
        String query = "미움받을 용기";

        // page 1
        BookSearchResult bookSearchResult = bookSearchClient.search(BookSearchClient.TARGET_TITLE, query, 1, 10);
        assertThat(bookSearchResult.getDocuments()).isNotNull();
        assertThat(bookSearchResult.getDocuments().size()).isGreaterThan(1);
        BookSearchResult.Document document1 = bookSearchResult.getDocuments().get(0);
        assertThat(document1.getTitle()).contains(query);

        // page 2
        if (!bookSearchResult.getMeta().isEnd()) {
            BookSearchResult bookSearchResult2 = bookSearchClient.search(BookSearchClient.TARGET_TITLE, query, 2, 10);
            assertThat(bookSearchResult2.getDocuments().get(0).getTitle()).contains(query);
        }
    }
}