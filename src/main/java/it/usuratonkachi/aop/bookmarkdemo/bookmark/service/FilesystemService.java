package it.usuratonkachi.aop.bookmarkdemo.bookmark.service;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.context.WrapperContext;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import it.usuratonkachi.aop.bookmarkdemo.exception.RetrievableException;
import it.usuratonkachi.aop.bookmarkdemo.utils.BookmarkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

@Service
@ConditionalOnProperty(prefix = "bookmark", value = "mode", havingValue = "fs", matchIfMissing = true)
public class FilesystemService extends BookmarkService {

    @Value("${bookmark.root.folder}")
    private Path bookmarkRootFolder;

    @PostConstruct
    private void init() {
        if (!bookmarkRootFolder.toFile().exists() && !bookmarkRootFolder.toFile().mkdirs())
            throw new RuntimeException("Cannot create root folder at " + bookmarkRootFolder);
    }

    @Override
    public Optional<Bookmark> getBookmark(WrapperContext wrapperContext) {
        String filename = bookmarkRootFolder.resolve(wrapperContext.getAddress()).resolve(wrapperContext.getId()).toString();
        try (FileInputStream file = new FileInputStream(filename)) {
            try (ObjectInputStream in = new ObjectInputStream(file)) {
                return Optional.ofNullable((Bookmark) in.readObject());
            }
        } catch (FileNotFoundException fnfe) {
            return Optional.empty();
        } catch (ClassNotFoundException | IOException ex){
            throw new RetrievableException(ex);
        }
    }

    @Override
    public Bookmark saveBookmark(WrapperContext wrapperContext, String bookmarkName, Class<?> dataType) {
        return saveBookmark(wrapperContext, bookmarkName, dataType, null);
    }

    @Override
    public Bookmark saveBookmark(WrapperContext wrapperContext, String bookmarkName, Class<?> dataType, BookmarkException bookmarkException) {
        Bookmark bookmark = BookmarkUtils.generateBookmark(wrapperContext, bookmarkName, dataType, bookmarkException);
        Path filePath = bookmarkRootFolder.resolve(wrapperContext.getAddress()).resolve(wrapperContext.getId());

        if (!filePath.getParent().toFile().exists() && !filePath.getParent().toFile().mkdirs())
            throw new RetrievableException(new RuntimeException("Cannot create bookmark dirs!"));

        try (FileOutputStream file = new FileOutputStream(filePath.toString())) {
            try (ObjectOutputStream out = new ObjectOutputStream(file)) {
                out.writeObject(bookmark);
                return bookmark;
            }
        } catch (IOException ex){
            throw new RetrievableException(ex);
        }
    }

}
