package woos.bookassist.api;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import woos.bookassist.api.hateoas.PaginatedResultsRetrievedEvent;
import woos.bookassist.api.v1.QueryRecommendV1;
import woos.bookassist.api.v1.SearchV1;
import woos.bookassist.common.exception.ResourceNotFoundException;
import woos.bookassist.domain.search.service.BookSearchService;
import woos.bookassist.remote.openapi.BookSearchResult;

import static woos.bookassist.remote.openapi.BookSearchResult.Document;

@RestController
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/book/search")
    public List<Document> searchBook(Principal principal,
                                     @RequestParam("query") String query,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                     UriComponentsBuilder uriBuilder,
                                     HttpServletResponse response) {
        BookSearchResult searchResult = bookSearchService.search(principal.getName(),
                query, makeOneBased(page), size);
        Page<Document> resultPage = new PageImpl<>(searchResult.getDocuments(), PageRequest.of(page, size),
                searchResult.getMeta().getTotalCount());
        if (page > resultPage.getTotalPages() || resultPage.getTotalPages() == 0) {
            throw new ResourceNotFoundException(query);
        }
        applicationEventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<Document>(
                Document.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent();
    }

    @GetMapping("/book/mysearches")
    public List<SearchV1> mySearches(Principal principal) {
        return bookSearchService.getUserSearches(principal.getName())
                .stream().map(searches -> modelMapper.map(searches, SearchV1.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/book/recommend")
    public List<QueryRecommendV1> recommend() {
        return bookSearchService.getTop10Queries()
                .stream().map(queryRecommend -> modelMapper.map(queryRecommend, QueryRecommendV1.class))
                .collect(Collectors.toList());
    }

    private int makeOneBased(int zeroBasedPage) {
        return zeroBasedPage + 1;
    }
}
