package it.usuratonkachi.aop.bookmarkdemo.bookmark.service;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import it.usuratonkachi.aop.bookmarkdemo.redis.repository.BookmarkRepository;
import it.usuratonkachi.aop.bookmarkdemo.utils.BookmarkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(FilesystemService.class)
@EnableRedisRepositories(basePackages = "it.usuratonkachi.aop.bookmarkdemo.redis.repository")
public class RedisService extends BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Override
    public Optional<Bookmark> getBookmark(Envelope wrapperContext) {
        return bookmarkRepository.findById(wrapperContext.getId());
    }

    @Override
    public Bookmark saveBookmark(Envelope wrapperContext, String bookmarkName, Class<?> dataType) {
        return saveBookmark(wrapperContext, bookmarkName, dataType, null);
    }

    @Override
    public Bookmark saveBookmark(Envelope wrapperContext, String bookmarkName, Class<?> dataType, BookmarkException bookmarkException) {
        Bookmark bookmark = BookmarkUtils.generateBookmark(wrapperContext, bookmarkName, dataType, bookmarkException);
        return bookmarkRepository.save(bookmark);
    }


}
