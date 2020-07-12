package woos.bookassist.remote.openapi;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NaverSearchResult {
    Date lastBuildDate;
    int total;
    int start;
    int display;
    List<Item> items;

    @Data
    public static class Item {
        String title;
        String link;
        String image;
        String author;
        int price;
        int discount;
        String publisher;
        String isbn;
        String description;
        Date pubdate;
    }
}
