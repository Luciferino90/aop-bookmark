package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import java.io.Serializable;

public interface IBookmarkData<T extends IBookmarkData<T>> extends Serializable {

    Bookmark<T> doBusinessLogicAndReturn(Bookmark<T> bookmark);

}
