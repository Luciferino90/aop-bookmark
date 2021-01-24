package it.usuratonkachi.aop.bookmarkdemo.exception;

import lombok.Getter;
import lombok.Setter;

public class RetryableException extends BookmarkException {

    @Getter @Setter
    private int retry = 0;

    public RetryableException(Throwable throwable) {
        super(throwable);
    }

    public RetryableException(Throwable throwable, int retry) {
        super(throwable);
        this.retry = retry;
    }

}
