package it.usuratonkachi.aop.bookmarkdemo.bookmark.service;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;

import java.util.Optional;

public abstract class BookmarkService {

    public abstract Optional<Bookmark> getBookmark(Envelope wrapperContext);
    public abstract Bookmark saveBookmark(Envelope wrapperContext, String bookmarkName, Class<?> dataType);
    public abstract Bookmark saveBookmark(Envelope wrapperContext, String bookmarkName, Class<?> dataType, BookmarkException bookmarkException);

}
