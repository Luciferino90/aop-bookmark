package it.usuratonkachi.aop.bookmarkdemo.context;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class Envelope {

    private String id;
    private String address;
    private Map<Class<?>, BookmarkData> bookmarkDataMap;


    public static Envelope factory(String address) {
        return Envelope.builder()
                .id(UUID.randomUUID().toString())
                .address(address)
                .build();
    }

}
