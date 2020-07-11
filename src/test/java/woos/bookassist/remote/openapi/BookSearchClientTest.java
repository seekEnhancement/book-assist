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
        BookSearchResult bookSearchResult = bookSearchClient.search(BookSearchClient.TARGET_TITLE, query);
        assertThat(bookSearchResult.getDocuments()).isNotNull();
        assertThat(bookSearchResult.getDocuments().size()).isGreaterThan(1);
        BookSearchResult.Document document1 = bookSearchResult.getDocuments().get(0);
        assertThat(document1.getTitle()).contains("미움받을 용기");
    }
}