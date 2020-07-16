package woos.bookassist.domain.search.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.test.StepVerifier;
import woos.bookassist.domain.search.repository.SearchRepository;
import woos.bookassist.domain.search.repository.Searches;
import woos.bookassist.remote.openapi.BookSearchClient;
import woos.bookassist.remote.openapi.BookSearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static woos.bookassist.remote.openapi.BookSearchResult.*;

public class BookSearchServiceMockTest {
    private BookSearchService bookSearchService;
    private BookSearchClient bookSearchClient;
    private SearchRepository searchRepository;

    @BeforeEach
    public void setup() {
        bookSearchClient = mock(BookSearchClient.class);
        searchRepository = mock(SearchRepository.class);
        bookSearchService = new BookSearchService(bookSearchClient, searchRepository);
    }

    @Test
    public void testSearch() {
        String userId = "woo";
        String query = "java";

        BookSearchResult mockResult = makeBookSearchResult(query);
        when(bookSearchClient.search(BookSearchClient.TARGET_TITLE, query, 1, 2)).thenReturn(mockResult);
        var result = bookSearchService.search(userId, query, 1, 2);
        assertThat(result).isEqualTo(mockResult);

        ArgumentCaptor<Searches> argument = ArgumentCaptor.forClass(Searches.class);
        verify(searchRepository, times(1)).save(argument.capture());
        assertThat(argument.getValue().getUserId()).isEqualTo(userId);
        assertThat(argument.getValue().getQuery()).isEqualTo(query);

        StepVerifier.create(bookSearchService.saveSearchHistoryAsync(userId, query))
                .verifyComplete();
    }

    private BookSearchResult makeBookSearchResult(String title) {
        return builder()
                .meta(Meta.builder().totalCount(4).end(false).pageableCount(3).build())
                .documents(List.of(makeDocument(title, 1), makeDocument(title, 2)))
                .build();
    }

    private Document makeDocument(String title, int i) {
        return Document.builder().title(String.format("%s_%s", title, i)).build();
    }
}
