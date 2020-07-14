package woos.bookassist.api.v1;

import lombok.Data;

@Data
public class QueryRecommendV1 {
    String query;
    Long queryCount;
}
