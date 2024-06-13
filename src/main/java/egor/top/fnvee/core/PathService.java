package egor.top.fnvee.core;

import egor.top.fnvee.swing.panel.PathsPanel;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service
public class PathService {

    public static final String _zip = "zip";
    public static final String _7z = "7z";
    public static final String _rar = "rar";

    @Value("${app.path.mo2}")
    private String mo2;
    @Value("${app.path.e}")
    private String e;

    @Autowired
    private PathsPanel pathsPanel;

    public Path[] getPaths(Path path, boolean isFnvee) {
        return Optional.ofNullable(path)
                .map(path1 -> isFnvee ? Paths.get(path1.toString(), mo2) : path1)
                .map(path2 -> {
                    log.debug(path2.toString());
                    try {
                        return Files.list(path2)
                                .filter(path4 -> !isFnvee || Files.isDirectory(path4))
                                .filter(path3 -> isFnvee ? path3.toString().startsWith(path2 + e) : StringUtils.endsWithAny(path3.toString(), _zip, _7z, _rar))
                                .toArray(Path[]::new);
                    } catch (IOException ex) {
                        return null;
                    }
                })
                .orElse(new Path[0]);
    }

    public void delete(Path path) {
        Optional.ofNullable(path)
                .ifPresent(_path -> {
                    try {
                        FileSystemUtils.deleteRecursively(_path);
                        log.warn("deleted '{}'", _path);
                    } catch (IOException e) {
                        log.error("Cannot delete", e);
                    }
                });
    }

    public void deleteAndRefresh(JList<Path> jList, JButton jButton) {
        if (ObjectUtils.anyNull(jList, jButton)) {
            return;
        }
        var selected = jList.getSelectedValuesList();
        if (!selected.isEmpty()) {
            selected.forEach(this::delete);
            jButton.doClick();
        }
    }

    public Path createFolder(Path newMod) {
        try {
            var newFolder = Files.createDirectory(Paths.get(
                    pathsPanel.getFnveePath().toString(),
                    mo2,
                    Optional.ofNullable(newMod)
                            .map(Path::toFile)
                            .map(File::getName)
                            .map(s -> StringUtils.removeEndIgnoreCase(s, _zip))
                            .map(s -> StringUtils.removeEndIgnoreCase(s, _7z))
                            .map(s -> StringUtils.removeEndIgnoreCase(s, _rar))
                            .map(s -> StringUtils.prependIfMissing(s, e))
                            .orElseThrow(IOException::new)
            ));
            log.info("created folder '{}'", newFolder);
            return newFolder;
        } catch (IOException e) {
            throw new RuntimeException("Cannot create folder", e);
        }
    }

    public boolean unZipAndCopyToFolder(Path newMod, Path newFolder) {
        if (ObjectUtils.anyNull(newMod, newFolder) || !StringUtils.endsWithIgnoreCase(newMod.toString(), _zip)) {
            return false;
        }

        try {
            ZipFile zipFile = new ZipFile(newMod.toFile());
            log.trace("+++ unzipping started ---");
            zipFile.extractAll(newFolder.toString());
            log.trace("+++ unzipping finished ---");
            return true;
        } catch (ZipException e) {
            log.error("cannot unzip", e);
            return false;
        }
    }

}
