package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step3_FetchResultAndSave extends BookmarkData {

    private Map<String, Nested> testObject = Map.of("One", new Nested(), "Two" ,new Nested());

    @Override
    public Envelope updateEnvelope(Envelope envelope) {
        return null;
    }

    @Override
    public BookmarkData updateBookmark(Envelope envelope) {
        return null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Nested {
        private String testString = "testString";
        private ZonedDateTime testDate = ZonedDateTime.now();
    }



}
