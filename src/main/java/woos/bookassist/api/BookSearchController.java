package woos.bookassist.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import woos.bookassist.api.hateoas.PaginatedResultsRetrievedEvent;
import woos.bookassist.common.exception.ResourceNotFoundException;
import woos.bookassist.remote.openapi.BookSearchClient;
import woos.bookassist.remote.openapi.BookSearchResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static woos.bookassist.remote.openapi.BookSearchResult.Document;

@RestController
public class BookSearchController {

    @Autowired
    private BookSearchClient bookSearchClient;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping(value = "/book/search")
    public List<Document> searchBook(@RequestParam("query") String query,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                     UriComponentsBuilder uriBuilder,
                                     HttpServletResponse response) {
        BookSearchResult searchResult = bookSearchClient.search(BookSearchClient.TARGET_TITLE, query,
                makeOneBased(page), size);
        Page<Document> resultPage = new PageImpl<>(searchResult.getDocuments(), PageRequest.of(page, size),
                searchResult.getMeta().getTotalCount());
        if (page > resultPage.getTotalPages() || resultPage.getTotalPages() == 0) {
            throw new ResourceNotFoundException(query);
        }
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Document>(
                Document.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent();
    }

    private int makeOneBased(int zeroBasedPage) {
        return zeroBasedPage + 1;
    }
}
