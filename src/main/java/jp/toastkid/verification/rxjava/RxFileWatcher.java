package jp.toastkid.verification.rxjava;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * File watcher implements by RxJava.
 *
 * You should add dependencies
 * compile 'io.reactivex:rxjava:1.1.0'.
 *
 * @author Toast kid
 *
 */
public class RxFileWatcher {

    /** backup interval. */
    private static final long BACKUP_INTERVAL = TimeUnit.SECONDS.toMillis(5L);

    /** file watcher target directory. */
    private static final String TARGET_DIR = "path/to/dir";

    /** Files map. */
    private static final Map<Path, Long> FILES = new HashMap<>();

    /**
     * main method.
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {

        initFiles();
        makeFileWatcher()
            .subscribeOn(Schedulers.newThread())
            .subscribe(path -> {
            System.out.println(LocalDateTime.now().toString() + " " + path.toString());
        });
        while (true) {
            try {
                System.out.printf("Sleep %dms\n", BACKUP_INTERVAL);
                Thread.sleep(BACKUP_INTERVAL);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * init files.
     * @throws IOException
     */
    private static void initFiles() throws IOException {
        final Path dir = Paths.get(TARGET_DIR);
        if (!Files.isDirectory(dir)) {
            return;
        }
        Files.list(dir)
            .filter(path -> !Files.isDirectory(path))
            .forEach(path -> {
                try {
                    FILES.put(path, Files.getLastModifiedTime(path).toMillis());
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });

    }

    /**
     * make file watching observable.Scheduler
     * @return Observable
     */
    private static Observable<Path> makeFileWatcher() {
        return Observable.create((sub) -> {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> sub.onCompleted()));
            while (true) {
                System.out.println("Start check last modified.");
                final File backup = new File("backup");
                if (!backup.exists() || !backup.isDirectory()) {
                    System.out.println("make backup dir.");
                    backup.mkdir();
                }
                FILES
                    .entrySet().stream()
                    .filter(entry -> {
                        try {
                            final long ms = Files.getLastModifiedTime(entry.getKey()).toMillis();
                            return entry.getValue() < ms;
                        } catch (final Exception e) {
                            sub.onError(e);
                        }
                        return false;
                    })
                    .forEach(entry -> {
                        try {
                            final long ms = Files.getLastModifiedTime(entry.getKey()).toMillis();
                            FILES.put(entry.getKey(), ms);
                        } catch (final Exception e) {
                            sub.onError(e);
                        }
                        sub.onNext(entry.getKey());
                    });
                try {
                    System.out.printf("Observable sleeping %dms\n", BACKUP_INTERVAL);
                    Thread.sleep(BACKUP_INTERVAL);
                } catch (final InterruptedException e) {
                    sub.onError(e);
                }
            }
        });
    }
}
