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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step1_Filtering implements IBookmarkData<Step1_Filtering> {

    private String testString = "testString";

    public static Bookmark<Step1_Filtering> createBookmarkData(String messageId) {
        return Bookmark.<Step1_Filtering>builder()
                .bookmarkStatus(BookmarkStatus.TODO)
                .error(BookmarkError.generateError(null))
                .expiration(Constants.REDIS_TTL)
                .id(BookmarkUtils.getBookmarkId(messageId, Step1_Filtering.class))
                .dataType(Step1_Filtering.class.getName())
                .data(new Step1_Filtering())
                .build();
    }

    @Override
    public Bookmark<Step1_Filtering> doBusinessLogicAndReturn(Bookmark<Step1_Filtering> bookmark) {
        bookmark.setBookmarkStatus(BookmarkStatus.DONE);
        return bookmark;
    }

}
