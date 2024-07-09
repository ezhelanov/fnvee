package egor.top.fnvee.core;

import egor.top.fnvee.core.un.UnService;
import egor.top.fnvee.swing.SwingUtil;
import egor.top.fnvee.swing.panel.PathsPanel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    private Set<UnService> unServices;
    @Autowired
    private SwingUtil swingUtil;

    public Path[] getPaths(Path path) {
        return Optional.ofNullable(path)
                .map(path2 -> {
                    try(var stream = Files.list(path2)) {
                        return stream.toArray(Path[]::new);
                    } catch (Exception e) {
                        log.error("[getPaths] cannot get paths", e);
                        return null;
                    }
                })
                .orElse(new Path[0]);
    }

    public Path[] getPaths(Path path, boolean isFnvee) {
        return Optional.ofNullable(path)
                .map(path1 -> isFnvee ? Paths.get(path1.toString(), mo2) : path1)
                .map(path2 -> {
                    try(var stream = Files.list(path2)) {
                        return stream
                                .filter(path4 -> !isFnvee || Files.isDirectory(path4))
                                .filter(path3 -> isFnvee ? path3.toString().startsWith(path2 + ePrefix) : StringUtils.endsWithAny(path3.toString(), _zip, _7z, _rar))
                                .toArray(Path[]::new);
                    } catch (Exception e) {
                        log.error("[getPaths] cannot get paths", e);
                        return null;
                    }
                })
                .orElse(new Path[0]);
    }

    public boolean deleteAndRefresh(JList<Path> jList, JButton jButton) {
        if (ObjectUtils.anyNull(jList, jList.getSelectedValue(), jButton)) {
            return false;
        }
        if (swingUtil.isDelete(jList.getSelectedValue())) {
            PathUtils.delete(jList.getSelectedValue());
            jButton.doClick();
            return true;
        }
        return false;
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
            log.info("[createFolderForNewMod] created folder {}", Strings.dquote(newFolder.toString()));
            return newFolder;
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException && Objects.nonNull(newMod)) {
                log.warn("[createFolderForNewMod] folder already exists for mod {}", Strings.dquote(newMod.toString()));
            } else {
                log.error("[createFolderForNewMod] cannot create folder", e);
            }
        }
        return null;
    }

    private void asFnvee(Path newFolder) {
        try {
            Files.walkFileTree(newFolder, new FnveeFileVisitor(newFolder));
            cleanup(newFolder);
        } catch (Exception e) {
            log.error("[asFnvee] cannot asFnvee", e);
        }
    }

    private void cleanup(Path newFolder) {
        log.trace("[cleanup] +++ cleanup +++");
        try(var stream = Files.list(newFolder)) {
            stream
                    .filter(path -> !StringUtils.endsWithAny(path.toString().toLowerCase(), FnveeFileVisitor.fnveeFiles) && !StringUtils.endsWithAny(path.toString().toLowerCase(), FnveeFileVisitor.fnveeFolders))
                    .forEach(PathUtils::delete);
            log.trace("[cleanup] --- cleanup ---");
        } catch (Exception e) {
            log.error("[cleanup] cannot cleanup", e);
        }
    }

    @Deprecated
    public void install(JList<Path> newMods, JButton eButton) {
        if (ObjectUtils.anyNull(newMods, eButton) || Objects.isNull(newMods.getSelectedValue())) {
            return;
        }

        var newFolder = createFolderForNewMod(newMods.getSelectedValue());
        if (Objects.isNull(newFolder)) {
            swingUtil.alreadyInstalled(newMods.getSelectedValue());
            return;
        }
        if (!IterableUtils.matchesAny(unServices, unService -> unService.extractAndCopyToFolder(newMods.getSelectedValue(), newFolder))) {
            PathUtils.delete(newFolder);
        } else {
            asFnvee(newFolder);
            eButton.doClick();
        }
    }

    public SwingWorker<Boolean, Void> getWorker(Path newMod, JButton eButton, JButton newButtonAdd, JProgressBar newProgressBar, JList<Path> eMods) {
        if (ObjectUtils.anyNull(newMod, eButton, newButtonAdd) || !swingUtil.isInstall(newMod)) {
            return null;
        }

        final var newFolder = createFolderForNewMod(newMod);
        if (Objects.isNull(newFolder)) {
            swingUtil.alreadyInstalled(newMod);
            return null;
        }
        newButtonAdd.setEnabled(false);
        newProgressBar.setIndeterminate(true);

        return new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return IterableUtils.matchesAny(unServices, unService -> unService.extractAndCopyToFolder(newMod, newFolder));
            }

            @Override
            protected void done() {
                try {
                    boolean installed = get();
                    if (!installed) {
                        PathUtils.delete(newFolder);
                    } else {
                        asFnvee(newFolder);
                        eButton.doClick();
                        eMods.setSelectedValue(newFolder, true);
                    }
                    newButtonAdd.setEnabled(true);
                    newProgressBar.setIndeterminate(false);
                    if (!installed) {
                        swingUtil.cannotInstall(newMod);
                    } else {
                        log.info("[SwingWorker.done] installed !");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("[SwingWorker.done] error", e);
                }
            }
        };
    }

}
