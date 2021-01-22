package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IFilteringBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step3_FetchResultAndSave implements IFilteringBookmarkData {

    private Map<String, Nested> testObject = Map.of("One", new Nested(), "Two" ,new Nested());

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Nested {
        private String testString = "testString";
        private ZonedDateTime testDate = ZonedDateTime.now();
    }

    public Step3_FetchResultAndSave(WrapperContext wrapperContext) {
        // Called by reflection
    }

    @Override
    public boolean filter(WrapperContext wrapperContext) {
        return true;
    }

    @Override
    public IBookmarkData updateBookmark(WrapperContext wrapperContext) {
        return this;
    }

}
