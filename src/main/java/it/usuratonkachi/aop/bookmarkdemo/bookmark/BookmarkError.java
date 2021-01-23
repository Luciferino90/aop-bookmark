package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
public class BookmarkError implements Serializable {

    private String cause;
    private BookmarkException exception;

    public static BookmarkError generateError(BookmarkException bookmarkException) {
        return Optional.ofNullable(bookmarkException)
                .map(exception -> BookmarkError.builder()
                        .cause(exception.getMessage())
                        .exception(exception)
                        .build())
                .orElse(null);
    }

}
