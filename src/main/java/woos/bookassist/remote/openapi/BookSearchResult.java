package woos.bookassist.remote.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class BookSearchResult {
    Meta meta;
    List<Document> documents;

    @Data
    public static class Document implements Serializable {
        private static final long serialVersionUID = -5930756756287719790L;
        String title;
        String contents;
        String url;
        String isbn;
        Date datetime;
        List<String> authors;
        String publisher;
        List<String> translators;
        long price;
        @JsonProperty("sale_price")
        long salePrice;
        String thumbnail;
        String status;
    }

    @Data
    public static class Meta {
        @JsonProperty("is_end")
        boolean end;
        @JsonProperty("pageable_count")
        int pageableCount;
        @JsonProperty("total_count")
        int totalCount;
    }
}
