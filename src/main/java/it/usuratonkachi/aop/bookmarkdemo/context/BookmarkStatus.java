package it.usuratonkachi.aop.bookmarkdemo.context;

public enum BookmarkStatus {
    TODO(0),
    INCOMPLETE(1),
    DONE(2);

    private int value;

    BookmarkStatus(int value) {
        this.value = value;
    }

}
