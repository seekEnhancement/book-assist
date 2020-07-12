package woos.bookassist.remote.openapi;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.encoding.FeignClientEncodingProperties;
import org.springframework.context.annotation.Bean;

public class NaverSearchClientFeignConfiguration {

    @Bean
    RequestInterceptor authorizationRequestInterceptor(@Value("${openapi.naver.clientId}") String clientId,
                                                       @Value("${openapi.naver.clientSecret}") String clientSecret) {
        return new NaverAuthenticationRequestInterceptor(new FeignClientEncodingProperties(),
                clientId, clientSecret);
    }
}
