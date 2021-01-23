package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Step1_Filtering extends BookmarkData {

    private String testString = "testString";

    @Override
    public Envelope updateEnvelope(Envelope envelope) {
        return null;
    }

    @Override
    public BookmarkData updateBookmark(Envelope envelope) {
        return null;
    }
}
