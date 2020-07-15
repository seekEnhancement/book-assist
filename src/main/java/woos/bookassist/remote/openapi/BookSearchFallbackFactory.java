package woos.bookassist.remote.openapi;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static woos.bookassist.remote.openapi.BookSearchResult.Document;
import static woos.bookassist.remote.openapi.BookSearchResult.Meta;

@Slf4j
@Component
public class BookSearchFallbackFactory implements FallbackFactory<BookSearchClient> {
    @Autowired
    NaverSearchClient naverSearchClient;

    @Override
    public BookSearchClient create(Throwable cause) {
        return new BookSearchClient() {
            @Override
            public BookSearchResult search(String target, String query, int page, int size) {
                log.error("[BookSearchClient.search hystrix error] query: {}, page: {}, size: {}",
                        query, page, size, cause);
                return convertNaverResultToKakaoResult(
                        naverSearchClient.search(query, convertPageToStart(page, size), size));
            }
        };
    }

    protected BookSearchResult convertNaverResultToKakaoResult(NaverSearchResult naverSearchResult) {
        int total = naverSearchResult.getTotal();
        boolean end = (total == naverSearchResult.getStart() + naverSearchResult.getDisplay());
        int pageableCount = Math.min(1000, total - 1);
        Meta meta = Meta.builder().end(end).pageableCount(pageableCount).totalCount(total).build();
        List<Document> documents = naverSearchResult.getItems().stream()
                .map(item -> makeDocument(item))
                .collect(Collectors.toList());
        return BookSearchResult.builder().meta(meta).documents(documents).build();
    }

    private Document makeDocument(NaverSearchResult.Item item) {
        return Document.builder()
                .title(item.getTitle())
                .contents(item.getDescription())
                .url(item.getLink())
                .isbn(item.getIsbn())
                .datetime(item.getPubdate())
                .authors(List.of(item.getAuthor()))
                .publisher(item.getPublisher())
                .price(item.getPrice())
                .salePrice(item.getDiscount())
                .thumbnail(item.getImage())
                .status(null)
                .build();
    }

    private int convertPageToStart(int page, int size) {
        if (page <= 1) {
            return 1;
        } else {
            return (page - 1) * size + 1;
        }
    }
}
