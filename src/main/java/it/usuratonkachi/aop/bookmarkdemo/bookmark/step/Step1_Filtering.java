package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Step1_Filtering implements IBookmarkData {

    private String testString = "testString";

    public Step1_Filtering(WrapperContext wrapperContext) {
        // Called by reflection
    }

    @Override
    public boolean filter(WrapperContext wrapperContext) {
        return true;
    }

    @Override
    public WrapperContext alter(WrapperContext wrapperContext) {
        return wrapperContext;
    }

    @Override
    public IBookmarkData updateBookmark(WrapperContext wrapperContext, String bookmarkName, Class<?> dataType) {
        return this;
    }

}
