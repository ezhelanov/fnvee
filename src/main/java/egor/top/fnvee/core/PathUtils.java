package egor.top.fnvee.core;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@UtilityClass
public class PathUtils {

    public static void delete(Path path) {
        Optional.ofNullable(path)
                .ifPresent(_path -> {
                    try {
                        FileSystemUtils.deleteRecursively(_path);
                        log.warn("[delete] deleted {}", Strings.dquote(_path.toString()));
                    } catch (IOException e) {
                        log.error("[delete] cannot delete", e);
                    }
                });
    }

    public static void copy(Path from, Path to) {
        log.trace("[copy] copying {} to {}", Strings.dquote(from.toString()), Strings.dquote(to.toString()));
        try {
            FileSystemUtils.copyRecursively(from, to);
            log.trace("[copy] copying finished");
        } catch (IOException e) {
            log.error("[copy] cannot copy", e);
        }
    }

}
