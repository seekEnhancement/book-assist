package woos.bookassist.remote.openapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao")
public interface BookSearchClient {
    String KAKAO_AK = "KakaoAK";
    String TARGET_TITLE = "title";

    @GetMapping("/v3/search/book")
    BookSearchResult search(@RequestHeader("Authorization") String token,
                            @RequestParam String target, @RequestParam String query);
}
