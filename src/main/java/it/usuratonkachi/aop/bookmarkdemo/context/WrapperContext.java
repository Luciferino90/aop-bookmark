package it.usuratonkachi.aop.bookmarkdemo.context;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class WrapperContext {

    private String id;
    private String address;
    private Bookmark<?> bookmark;


    public static WrapperContext factory(String address) {
        return WrapperContext.builder()
                .id(UUID.randomUUID().toString())
                .address(address)
                .build();
    }

}
