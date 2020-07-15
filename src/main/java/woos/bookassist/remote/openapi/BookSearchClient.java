package woos.bookassist.remote.openapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao", configuration = BookSearchClientFeignConfiguration.class
        , fallbackFactory = BookSearchFallbackFactory.class)
public interface BookSearchClient {
    String TARGET_TITLE = "title";

    @GetMapping("/v3/search/book")
    BookSearchResult search(@RequestParam(defaultValue = TARGET_TITLE) String target, @RequestParam String query,
                            @RequestParam int page, @RequestParam int size);
}
