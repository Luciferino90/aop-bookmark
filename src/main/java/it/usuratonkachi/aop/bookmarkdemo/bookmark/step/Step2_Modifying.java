package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class Step2_Modifying implements IBookmarkData {

    private ZonedDateTime date = ZonedDateTime.now();

    public Step2_Modifying(WrapperContext wrapperContext) {
        // Called by reflection
    }

    @Override
    public WrapperContext alter(WrapperContext wrapperContext) {
        return wrapperContext;
    }

    @Override
    public boolean filter(WrapperContext wrapperContext) {
        return true;
    }

    @Override
    public IBookmarkData updateBookmark(WrapperContext wrapperContext, String bookmarkName, Class<?> dataType) {
        return this;
    }

}
