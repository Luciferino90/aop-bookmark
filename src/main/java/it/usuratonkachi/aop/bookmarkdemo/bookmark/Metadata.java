package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Metadata implements Serializable {

    private String bookmarkName;
    private Class<?> dataType;

}
