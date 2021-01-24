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
        String messageId = UUID.randomUUID().toString();
        return Envelope.builder()
                .id(messageId)
                .address(address)
                .bookmarkDataMap(BookmapFactory.create(messageId))
                .build();
    }

    public Bookmark<? extends IBookmarkData<?>> getBookmark(String getClassName) {
        return Optional.ofNullable(bookmarkDataMap)
                .map(bookmarkDataMap -> bookmarkDataMap.get(getClassName))
                .orElseThrow(() -> new RuntimeException("No bookmark found for className " + getClassName));
    }

}
