package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class Step2_Modifying extends BookmarkData {

    private ZonedDateTime date = ZonedDateTime.now();


    @Override
    public Envelope updateEnvelope(Envelope envelope) {
        return null;
    }

    @Override
    public BookmarkData updateBookmark(Envelope envelope) {
        return null;
    }
}
