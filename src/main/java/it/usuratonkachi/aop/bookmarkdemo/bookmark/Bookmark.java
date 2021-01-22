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
public class Bookmark implements Serializable {

    @Indexed
    private String id;
    private Metadata meta;
    private IBookmarkData data;
    private BookmarkError error;

    public boolean filter(WrapperContext wrapperContext) {
        return data.filter(wrapperContext);
    }

    public IBookmarkData updateBookmark(WrapperContext wrapperContext) {
        return data.updateBookmark(wrapperContext);
    }

    public WrapperContext alter(WrapperContext wrapperContext) {
        if (data instanceof IAlteringAndFilteringBookmarkData)
            return ((IAlteringAndFilteringBookmarkData)data).alter(wrapperContext);
        return wrapperContext;
    }

}
