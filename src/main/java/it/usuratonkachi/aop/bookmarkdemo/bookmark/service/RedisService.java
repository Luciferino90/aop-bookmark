package it.usuratonkachi.aop.bookmarkdemo.bookmark.service;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import it.usuratonkachi.aop.bookmarkdemo.redis.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(FilesystemService.class)
@EnableRedisRepositories(basePackages = "it.usuratonkachi.aop.bookmarkdemo.redis.repository")
public class RedisService<T extends IBookmarkData<T>> extends BookmarkService<T> {

    private final BookmarkRepository<T> bookmarkRepository;

    @Override
    public Optional<Bookmark<T>> getBookmark(Envelope wrapperContext) {
        return bookmarkRepository.findById(wrapperContext.getId());
    }

    @Override
    public Bookmark<T> saveBookmark(Envelope envelope, Bookmark<T> bookmark) {
        return saveBookmark(envelope, bookmark, null);
    }

    @Override
    public Bookmark<T> saveBookmark(Envelope wrapperContext, Bookmark<T> bookmark, BookmarkException bookmarkException) {
        bookmark.setError(BookmarkError.generateError(bookmarkException));
        return bookmarkRepository.save(bookmark);
    }

}
