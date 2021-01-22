package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@Builder
@RedisHash("Bookmark")
public class Event<T extends IBookmarkData> implements Serializable {

    @Indexed
    private String id;
    private Metadata meta;
    private T data;
    private BookmarkError error;

    public boolean filter(WrapperContext wrapperContext) {
        return data.filter(wrapperContext);
    }

    public IBookmarkData updateBookmark(WrapperContext wrapperContext, String bookmarkName, Class<?> dataType) {
        return data.updateBookmark(wrapperContext, bookmarkName, dataType);
    }

    public WrapperContext alter(WrapperContext wrapperContext) {
        data.alter(wrapperContext);
        return wrapperContext;
    }

}
