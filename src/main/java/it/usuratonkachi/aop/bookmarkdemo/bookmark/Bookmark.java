package it.usuratonkachi.aop.bookmarkdemo.bookmark;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@RedisHash("Bookmark")
public class Bookmark implements Serializable {

    private String id;
    private Map<String, Event<?>> bookmarkHistory;

}
