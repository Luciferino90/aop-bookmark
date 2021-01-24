package it.usuratonkachi.aop.bookmarkdemo.context;

public enum BookmarkStatus {
    TODO(0),
    DONE(1),
    INCOMPLETE(2),
    ERROR(3);

    private int value;

    BookmarkStatus(int value) {
        this.value = value;
    }

}
