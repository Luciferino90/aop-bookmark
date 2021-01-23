package it.usuratonkachi.aop.bookmarkdemo.context;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.step.BookmapFactory;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Envelope {

    private String id;
    private String address;
    private Map<String, Bookmark<? extends IBookmarkData<?>>> bookmarkDataMap;

    public static Envelope factory(String address) {
        return Envelope.builder()
                .id(UUID.randomUUID().toString())
                .address(address)
                .bookmarkDataMap(BookmapFactory.create())
                .build();
    }

    public Bookmark<? extends IBookmarkData<?>> getBookmark(String stepKey) {
        return Optional.ofNullable(bookmarkDataMap)
                .map(bookmarkDataMap -> bookmarkDataMap.get(stepKey))
                .orElseThrow(() -> new RuntimeException("No bookmark found for stepKey " + stepKey));
    }

}
