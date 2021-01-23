package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Metadata;
import it.usuratonkachi.aop.bookmarkdemo.context.BookmarkStatus;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Step1_Filtering implements IBookmarkData<Step1_Filtering> {

    private String testString = "testString";

    public static Bookmark<Step1_Filtering> createBookmarkData() {
        return Bookmark.<Step1_Filtering>builder()
                .id(UUID.randomUUID().toString())
                .bookmarkStatus(BookmarkStatus.TODO)
                .meta(Metadata.builder()
                        .bookmarkName(Step1_Filtering.class.getName())
                        .dataType(Step1_Filtering.class)
                        .build())
                .error(BookmarkError.generateError(null))
                .data(new Step1_Filtering())
                .build();
    }

}
