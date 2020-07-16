package woos.bookassist.api.v1;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SearchV1 {
    private Long searchNumber;
    private String userId;
    private String query;
    private LocalDateTime searchDateTime;
}
