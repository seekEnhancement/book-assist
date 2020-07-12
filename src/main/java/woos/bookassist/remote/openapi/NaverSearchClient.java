package woos.bookassist.remote.openapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naver", configuration = NaverSearchClientFeignConfiguration.class)
public interface NaverSearchClient {

    @GetMapping("/v1/search/book.json")
    NaverSearchResult search(@RequestParam String query,
                             @RequestParam int start, @RequestParam int display);
}
