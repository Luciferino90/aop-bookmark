package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;

public interface IAlteringAndFilteringBookmarkData extends IBookmarkData {

    WrapperContext alter(WrapperContext wrapperContext);

}
