package woos.bookassist.remote.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ActiveProfiles("kakaoerror")
@SpringBootTest
@DirtiesContext
class BookSearchClientFallbackTest {

    @Autowired
    BookSearchClient bookSearchClient;

    @SpyBean
    BookSearchFallbackFactory bookSearchFallbackFactory;

    @Test
    public void testSearchFallback() {
        String query = "미움받을 용기";

        // application-kakaoerror.yml -> java.net.UnknownHostException: nonexisting.kakao.com
        // bookSearchFallbackFactory's fallback executing -> naverSearchClient.search -> convert to kakao result
        BookSearchResult bookSearchResult = bookSearchClient.search(BookSearchClient.TARGET_TITLE, query, 1, 10);
        assertThat(bookSearchResult.getDocuments()).isNotNull();
        assertThat(bookSearchResult.getDocuments().size()).isGreaterThan(1);
        BookSearchResult.Document document1 = bookSearchResult.getDocuments().get(0);
        assertThat(document1.getTitle()).contains(query);

        verify(bookSearchFallbackFactory).create(any(Throwable.class));
        verify(bookSearchFallbackFactory).convertNaverResultToKakaoResult(any(NaverSearchResult.class));
    }
}