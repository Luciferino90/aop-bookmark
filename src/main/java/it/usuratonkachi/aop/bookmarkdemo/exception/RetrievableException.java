package it.usuratonkachi.aop.bookmarkdemo.exception;

import lombok.Getter;

public class RetrievableException extends BookmarkException {

    @Getter
    private int retry = 0;

    public RetrievableException(Throwable throwable) {
        super(throwable);
    }

    public RetrievableException(Throwable throwable, int retry) {
        super(throwable);
        this.retry = retry;
    }

}
