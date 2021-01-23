package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;

import java.io.Serializable;

public interface IBookmarkData extends Serializable {

    Envelope updateEnvelope(Envelope envelope);
    IBookmarkData updateBookmark(Envelope envelope);

}
