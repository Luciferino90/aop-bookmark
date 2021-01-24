package it.usuratonkachi.aop.bookmarkdemo.redis.repository;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import org.springframework.data.repository.CrudRepository;

public interface BookmarkRepository<T extends IBookmarkData<T>> extends CrudRepository<Bookmark<T>, String> {

}
