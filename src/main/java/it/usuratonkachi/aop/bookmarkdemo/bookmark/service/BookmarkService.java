package it.usuratonkachi.aop.bookmarkdemo.bookmark.service;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;

import java.util.Optional;

public abstract class BookmarkService<T extends IBookmarkData<T>> {

    public abstract Optional<Bookmark<T>> getBookmark(Envelope envelope, Class<T> dataType);
    public abstract Bookmark<T> saveBookmark(Envelope envelope, Bookmark<T> bookmark);
    public abstract Bookmark<T> saveBookmark(Envelope envelope, Bookmark<T> bookmark, BookmarkException bookmarkException);
    public abstract Bookmark<T> deleteBookmarks(Envelope envelope, Bookmark<T> bookmark);

}
