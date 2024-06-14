package egor.top.fnvee.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public final class FnveeFileVisitor extends SimpleFileVisitor<Path> {

    public static final String[] fnveeFolders = new String[]{"textures", "meshes", "config", "nvse", "menus"};
    public static final Set<String> fnveeFoldersSet = SetUtils.hashSet(fnveeFolders);
    public static final String[] fnveeFiles = new String[]{".esp", ".esm"};

    private final Path newFolder;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        String fnveeFolder = IterableUtils.find(
                fnveeFoldersSet, _fnveeFolder -> StringUtils.endsWithIgnoreCase(dir.toString(), _fnveeFolder)
        );
        if (Objects.isNull(fnveeFolder) || newFolder.equals(dir)) {
            log.trace("opening {}", Strings.dquote(dir.toString()));
            return super.preVisitDirectory(dir, attrs);
        }
        log.info("found dir {}", Strings.dquote(dir.toString()));

        Path fnveeFolderPath = Paths.get(newFolder.toString(), fnveeFolder);
        if (!dir.equals(fnveeFolderPath)) {
            PathUtils.copy(dir, fnveeFolderPath);
            PathUtils.delete(dir);
        } else {
            log.debug("dir already in root {}", Strings.dquote(dir.toString()));
        }

        log.trace("skipping {}", Strings.dquote(dir.toString()));
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String fileName = file.toFile()
                .getName()
                .toLowerCase(); // пример: 'somefile.esm'
        if (StringUtils.endsWithAny(fileName, fnveeFiles)) {
            log.info("found file {}", Strings.dquote(file.toString()));
            Path fnveeFilePath = Paths.get(newFolder.toString(), fileName);
            PathUtils.copy(file, fnveeFilePath);
            PathUtils.delete(file);
        }
        return super.visitFile(file, attrs);
    }
}
