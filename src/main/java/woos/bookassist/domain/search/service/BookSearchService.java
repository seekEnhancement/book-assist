package woos.bookassist.domain.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import woos.bookassist.domain.search.repository.QueryRecommend;
import woos.bookassist.domain.search.repository.SearchRepository;
import woos.bookassist.domain.search.repository.Searches;
import woos.bookassist.remote.openapi.BookSearchClient;
import woos.bookassist.remote.openapi.BookSearchResult;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        saveSearchHistoryAsync(userId, query).subscribe();
        return searchResult;
    }

    protected Mono<Void> saveSearchHistoryAsync(String userId, String query) {
        log.debug("== BookSearchService.saveSearchHistoryAsync called.");
        return Mono.fromCallable(() -> {
            log.debug("== Mono inner searchRepository.save before.");
            searchRepository.save(Searches.builder().query(query).userId(userId).searchDateTime(LocalDateTime.now()).build());
            log.debug("== Mono inner searchRepository.save after.");
            return Mono.empty();
        }).subscribeOn(Schedulers.elastic()).then();
    }

    public List<Searches> getUserSearches(String userId) {
        return searchRepository.findByUserIdOrderBySearchDateTimeDesc(userId);
    }

    @Cacheable(cacheNames = "top10Queries")
    public List<QueryRecommend> getTop10Queries() {
        return searchRepository.findTop10Queries();
    }
}
