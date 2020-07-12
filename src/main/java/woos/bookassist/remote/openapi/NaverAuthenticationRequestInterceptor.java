package woos.bookassist.remote.openapi;

import feign.RequestTemplate;
import org.springframework.cloud.openfeign.encoding.BaseRequestInterceptor;
import org.springframework.cloud.openfeign.encoding.FeignClientEncodingProperties;

public class NaverAuthenticationRequestInterceptor extends BaseRequestInterceptor {
    public static final String NAVER_CLIENT_ID_HEADER = "X-Naver-Client-Id";
    public static final String NAVER_CLIENT_SECRET_HEADER = "X-Naver-Client-Secret";
    private String naverClientId;
    private String naverClientSecret;

    public NaverAuthenticationRequestInterceptor(FeignClientEncodingProperties properties,
                                                 String naverClientId, String naverClientSecret) {
        super(properties);
        this.naverClientId = naverClientId;
        this.naverClientSecret = naverClientSecret;
    }

    @Override
    public void apply(RequestTemplate template) {
        addHeader(template, NAVER_CLIENT_ID_HEADER, this.naverClientId);
        addHeader(template, NAVER_CLIENT_SECRET_HEADER, this.naverClientSecret);
    }
}
