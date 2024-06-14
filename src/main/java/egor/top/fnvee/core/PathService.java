package egor.top.fnvee.core;

import egor.top.fnvee.swing.panel.PathsPanel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public static final String _zip = ".zip";
    public static final String _7z = ".7z";
    public static final String _rar = ".rar";

    @Value("${app.path.mo2}")
    private String mo2;
    @Value("${app.path.e}")
    private String ePrefix;

    @Autowired
    private PathsPanel pathsPanel;
    @Autowired
    private UnZipService unZipService;

    public Path[] getPaths(Path path, boolean isFnvee) {
        return Optional.ofNullable(path)
                .map(path1 -> isFnvee ? Paths.get(path1.toString(), mo2) : path1)
                .map(path2 -> {
                    try {
                        return Files.list(path2)
                                .filter(path4 -> !isFnvee || Files.isDirectory(path4))
                                .filter(path3 -> isFnvee ? path3.toString().startsWith(path2 + ePrefix) : StringUtils.endsWithAny(path3.toString(), _zip, _7z, _rar))
                                .toArray(Path[]::new);
                    } catch (IOException ex) {
                        return null;
                    }
                })
                .orElse(new Path[0]);
    }

    public void deleteAndRefresh(JList<Path> jList, JButton jButton) {
        if (ObjectUtils.anyNull(jList, jButton)) {
            return;
        }
        var selected = jList.getSelectedValuesList();
        if (!selected.isEmpty()) {
            selected.forEach(PathUtils::delete);
            jButton.doClick();
        }
    }

    private Path createFolderForNewMod(Path newMod) {
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
                            .map(s -> StringUtils.prependIfMissing(s, ePrefix))
                            .orElseThrow(IOException::new)
            ));
            log.info("created folder {}", Strings.dquote(newFolder.toString()));
            return newFolder;
        } catch (IOException e) {
            throw new RuntimeException("cannot create folder", e);
        }
    }

    private void asFnvee(Path newFolder) {
        try {
            Files.walkFileTree(newFolder, new FnveeFileVisitor(newFolder));
            log.trace("+++ cleanup +++");
            Files.list(newFolder)
                    .filter(path -> !StringUtils.endsWithAny(path.toString(), FnveeFileVisitor.fnveeFiles) && !StringUtils.endsWithAny(path.toString(), FnveeFileVisitor.fnveeFolders))
                    .forEach(PathUtils::delete);
            log.trace("--- cleanup ---");
        } catch (IOException e) {
            log.error("cannot asFnvee", e);
        }
    }

    public void install(JList<Path> jList, JButton jButton) {
        if (ObjectUtils.anyNull(jList, jButton) || jList.getSelectedValuesList().isEmpty()) {
            return;
        }
        for (Path path : jList.getSelectedValuesList()) {
            var newFolder = createFolderForNewMod(path);
            if (!unZipService.unZipAndCopyToFolder(path, newFolder)) {
                PathUtils.delete(newFolder);
            } else {
                asFnvee(newFolder);
                jButton.doClick();
            }
        }
    }

}
