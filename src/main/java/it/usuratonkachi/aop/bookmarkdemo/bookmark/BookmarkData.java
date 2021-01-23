package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import it.usuratonkachi.aop.bookmarkdemo.context.BookmarkStatus;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;

import java.io.Serializable;

public abstract class BookmarkData implements IBookmarkData {

    private BookmarkStatus bookmarkStatus;

    public abstract Envelope updateEnvelope(Envelope envelope) {
        envelope.getBookmarkDataMap()
    }
    public abstract BookmarkData updateBookmark(Envelope envelope);

}
