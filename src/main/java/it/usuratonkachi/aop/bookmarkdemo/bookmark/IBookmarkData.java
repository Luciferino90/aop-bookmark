package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;

import java.io.Serializable;

public interface IBookmarkData extends Serializable {

    boolean filter(WrapperContext wrapperContext);
    IBookmarkData updateBookmark(WrapperContext wrapperContext);

}