package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.config.Constants;
import it.usuratonkachi.aop.bookmarkdemo.context.BookmarkStatus;
import it.usuratonkachi.aop.bookmarkdemo.utils.BookmarkUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step2_Date implements IBookmarkData<Step2_Date> {

    private ZonedDateTime date = ZonedDateTime.now();

    public static Bookmark<Step2_Date> createBookmarkData(String messageId) {
        return Bookmark.<Step2_Date>builder()
                .bookmarkStatus(BookmarkStatus.TODO)
                .error(BookmarkError.generateError(null))
                .expiration(Constants.REDIS_TTL)
                .id(BookmarkUtils.getBookmarkId(messageId, Step2_Date.class))
                .dataType(Step2_Date.class.getName())
                .data(new Step2_Date())
                .build();
    }

    @Override
    public Bookmark<Step2_Date> doBusinessLogicAndReturn(Bookmark<Step2_Date> bookmark) {
        bookmark.setBookmarkStatus(BookmarkStatus.DONE);
        return bookmark;
    }

}
