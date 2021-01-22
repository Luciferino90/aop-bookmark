package it.usuratonkachi.aop.bookmarkdemo.redis.repository;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookmarkRepository extends CrudRepository<Bookmark, String> {

    Optional<Bookmark> findById(String token);

}
