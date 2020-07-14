package woos.bookassist.api.v1;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchV1 {
    private Long searchNumber;
    private String userId;
    private String query;
    private LocalDateTime searchDateTime;
}
