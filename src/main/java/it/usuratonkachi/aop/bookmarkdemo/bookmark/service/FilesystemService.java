package it.usuratonkachi.aop.bookmarkdemo.bookmark.service;

import it.usuratonkachi.aop.bookmarkdemo.bookmark.Bookmark;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.BookmarkError;
import it.usuratonkachi.aop.bookmarkdemo.bookmark.IBookmarkData;
import it.usuratonkachi.aop.bookmarkdemo.context.Envelope;
import it.usuratonkachi.aop.bookmarkdemo.exception.BookmarkException;
import it.usuratonkachi.aop.bookmarkdemo.exception.RetryableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

@Service
@ConditionalOnProperty(prefix = "bookmark", value = "mode", havingValue = "fs", matchIfMissing = true)
public class FilesystemService<T extends IBookmarkData<T>> extends BookmarkService<T> {

    @Value("${bookmark.root.folder}")
    private Path bookmarkRootFolder;

    @PostConstruct
    private void init() {
        if (!bookmarkRootFolder.toFile().exists() && !bookmarkRootFolder.toFile().mkdirs())
            throw new RuntimeException("Cannot create root folder at " + bookmarkRootFolder);
    }

    protected Path calculateFilename(Envelope envelope, Class<?> dataType) {
        return bookmarkRootFolder.resolve(envelope.getAddress()).resolve(envelope.getId()).resolve(dataType.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Bookmark<T>> getBookmark(Envelope envelope, Class<T> dataType) {
        String filename = calculateFilename(envelope, dataType).toString();
        try (FileInputStream file = new FileInputStream(filename)) {
            try (ObjectInputStream in = new ObjectInputStream(file)) {
                return Optional.ofNullable((Bookmark<T>) in.readObject());
            }
        } catch (FileNotFoundException fnfe) {
            return Optional.empty();
        } catch (ClassNotFoundException | IOException ex){
            throw new RetryableException(ex);
        }
    }

    public Bookmark<T> saveBookmark(Envelope envelope, Bookmark<T> bookmark) {
        return saveBookmark(envelope, bookmark, null);
    }

    @Override
    public Bookmark<T> saveBookmark(Envelope envelope, Bookmark<T> bookmark, BookmarkException bookmarkException) {
        Path filePath = calculateFilename(envelope, bookmark.getData().getClass());
        bookmark.setError(BookmarkError.generateError(bookmarkException));
        if (!filePath.getParent().toFile().exists() && !filePath.getParent().toFile().mkdirs())
            throw new RetryableException(new RuntimeException("Cannot create bookmark dirs!"));

        try (FileOutputStream file = new FileOutputStream(filePath.toString())) {
            try (ObjectOutputStream out = new ObjectOutputStream(file)) {
                out.writeObject(bookmark);
                return bookmark;
            }
        } catch (IOException ex){
            throw new RetryableException(ex);
        }
    }

    @Override
    public Bookmark<T> deleteBookmarks(Envelope envelope, Bookmark<T> bookmark) {
        Path filePath = calculateFilename(envelope, bookmark.getData().getClass());
        Mono.just(filePath.getParent().toFile()).doOnNext(FileSystemUtils::deleteRecursively).subscribe();
        return bookmark;
    }
}
