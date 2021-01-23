package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.Metadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step3_Object implements IBookmarkData<Step3_Object> {

    private Map<String, Nested> testObject = Map.of("One", new Nested(), "Two" ,new Nested());

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Nested {
        private String testString = "testString";
        private ZonedDateTime testDate = ZonedDateTime.now();
    }

    public static Bookmark<Step3_Object> createBookmarkData() {
        return Bookmark.<Step3_Object>builder()
                .id(UUID.randomUUID().toString())
                .meta(Metadata.builder()
                        .bookmarkName(Step3_Object.class.getName())
                        .dataType(Step3_Object.class)
                        .build())
                .error(BookmarkError.generateError(null))
                .data(new Step3_Object())
                .build();
    }

}
