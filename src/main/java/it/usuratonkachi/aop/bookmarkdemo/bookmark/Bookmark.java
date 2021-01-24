package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import it.usuratonkachi.aop.bookmarkdemo.context.BookmarkStatus;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Bookmark")
public class Bookmark<T extends IBookmarkData<T>> implements Serializable {

    @Indexed
    private String id;
    private String dataType;
    private T data;
    private BookmarkError error;
    private BookmarkStatus bookmarkStatus;
    @TimeToLive
    private Long expiration;

    public Bookmark<T> updateEnvelope(Envelope envelope) {
        Optional.ofNullable(envelope.getBookmarkDataMap())
                .map(bookmarkDataMap -> bookmarkDataMap.put(data.getClass().getName(), this))
                .orElseThrow(() -> new RuntimeException("No bookmark found in envelope"));
        return this;
    }

    @SuppressWarnings("unchecked")
    public Bookmark<T> updateBookmark(Envelope envelope) {
        return (Bookmark<T>) Optional.ofNullable(envelope.getBookmarkDataMap())
                .map(bookmarkMap -> bookmarkMap.get(data.getClass().getName()))
                .map(bookmark -> bookmark.getData().doBusinessLogicAndReturn(bookmark))
                .orElseThrow(() -> new RuntimeException("No bookmark found in envelope"));
    }

}
