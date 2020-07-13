package woos.bookassist.domain.search.service;

import org.springframework.stereotype.Service;
import woos.bookassist.domain.search.repository.SearchRepository;
import woos.bookassist.domain.search.repository.Searches;
import woos.bookassist.remote.openapi.BookSearchClient;
import woos.bookassist.remote.openapi.BookSearchResult;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookSearchService {
    private final BookSearchClient bookSearchClient;
    private final SearchRepository searchRepository;

    public BookSearchService(BookSearchClient bookSearchClient, SearchRepository searchRepository) {
        this.bookSearchClient = bookSearchClient;
        this.searchRepository = searchRepository;
    }

    public BookSearchResult search(String userId, String query, int page, int size) {
        BookSearchResult searchResult = bookSearchClient.search(BookSearchClient.TARGET_TITLE, query, page, size);
        searchRepository.save(Searches.builder().query(query).userId(userId).searchDateTime(LocalDate.now()).build());
        return searchResult;
    }

    public List<Searches> getUserSearches(String userId) {
        return searchRepository.findByUserIdOrderBySearchDateTimeDesc(userId);
    }
}
