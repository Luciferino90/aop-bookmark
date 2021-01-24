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

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step3_Object implements IBookmarkData<Step3_Object> {

    private Map<String, Nested> failedObject = Map.of("One", new Nested(), "Two" , new Nested());

    @Override
    public Bookmark<Step3_Object> doBusinessLogicAndReturn(Bookmark<Step3_Object> bookmark) {
        bookmark.setBookmarkStatus((failedObject != null && failedObject.size() == 0) ? BookmarkStatus.DONE : BookmarkStatus.INCOMPLETE);
        return bookmark;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Nested implements Serializable {
        private String testString = "testString";
        private ZonedDateTime testDate = ZonedDateTime.now();
    }

    public static Bookmark<Step3_Object> createBookmarkData(String messageId) {
        return Bookmark.<Step3_Object>builder()
                .bookmarkStatus(BookmarkStatus.TODO)
                .error(BookmarkError.generateError(null))
                .expiration(Constants.REDIS_TTL)
                .id(BookmarkUtils.getBookmarkId(messageId, Step3_Object.class))
                .dataType(Step3_Object.class.getName())
                .data(new Step3_Object())
                .build();
    }

}
