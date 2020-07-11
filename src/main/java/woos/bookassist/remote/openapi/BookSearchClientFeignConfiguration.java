package woos.bookassist.remote.openapi;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.encoding.FeignClientEncodingProperties;
import org.springframework.context.annotation.Bean;

public class BookSearchClientFeignConfiguration {
    public static final String KAKAO_AK = "KakaoAK";

    @Bean
    RequestInterceptor authorizationRequestInterceptor(@Value("${openapi.kakao.appKey}") String kakaoAppKey) {
        return new AuthorizationRequestInterceptor(new FeignClientEncodingProperties(),
                String.format("%s %s", KAKAO_AK, kakaoAppKey));
    }
}
