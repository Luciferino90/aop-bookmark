package it.usuratonkachi.aop.bookmarkdemo.bookmark.step;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;

import java.util.HashMap;
import java.util.Map;

public class BookmapFactory {

    public static Map<String, Bookmark<? extends IBookmarkData<? extends IBookmarkData<?>>>> create(String messageId) {
        return new HashMap<>(
                Map.of(
                        Step1_Filtering.class.getName(), Step1_Filtering.createBookmarkData(messageId),
                        Step2_Date.class.getName(), Step2_Date.createBookmarkData(messageId),
                        Step3_Object.class.getName(), Step3_Object.createBookmarkData(messageId)
                )
        );
    }

}
