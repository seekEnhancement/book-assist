package woos.bookassist.remote.openapi;

import feign.RequestTemplate;
import org.springframework.cloud.openfeign.encoding.BaseRequestInterceptor;
import org.springframework.cloud.openfeign.encoding.FeignClientEncodingProperties;
import org.springframework.http.HttpHeaders;

public class AuthorizationRequestInterceptor extends BaseRequestInterceptor {
    private String authorization;

    public AuthorizationRequestInterceptor(FeignClientEncodingProperties properties, String authorization) {
        super(properties);
        this.authorization = authorization;
    }

    @Override
    public void apply(RequestTemplate template) {
        addHeader(template, HttpHeaders.AUTHORIZATION, authorization);
    }
}
