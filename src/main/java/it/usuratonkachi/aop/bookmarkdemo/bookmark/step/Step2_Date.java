package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Metadata;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Step2_Date implements IBookmarkData<Step2_Date> {

    private ZonedDateTime date = ZonedDateTime.now();

    public static Bookmark<Step2_Date> createBookmarkData() {
        return Bookmark.<Step2_Date>builder()
                .id(UUID.randomUUID().toString())
                .meta(Metadata.builder()
                        .bookmarkName(Step2_Date.class.getName())
                        .dataType(Step2_Date.class)
                        .build())
                .error(BookmarkError.generateError(null))
                .data(new Step2_Date())
                .build();
    }

}
