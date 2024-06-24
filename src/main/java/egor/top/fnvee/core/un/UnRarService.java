package egor.top.fnvee.core.un;

import com.aspose.zip.RarArchive;
import com.github.junrar.extract.ExtractArchive;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static egor.top.fnvee.core.PathService._rar;

@Slf4j
@Service
public class UnRarService extends UnService {

    @Override
    public boolean extractAndCopyToFolder(Path newMod, Path newFolder) {
        if (ObjectUtils.anyNull(newMod, newFolder) || !StringUtils.endsWithIgnoreCase(newMod.toString(), _rar)) {
            return false;
        }

        try {
            log.trace("[unRar: junrar] +++ unRar +++");
            ExtractArchive extractArchive = new ExtractArchive();
            extractArchive.extractArchive(newMod.toFile(), newFolder.toFile());
            log.trace("[unRar: junrar] --- unRar ---");
            return true;
        } catch (Exception e) {
            log.error("[unRar: junrar] cannot unRar", e);
            return extractAndCopyToFolderViaAspose(newMod, newFolder);
        }
    }

    private boolean extractAndCopyToFolderViaAspose(Path newMod, Path newFolder) {
        log.trace("[unRar: aspose] +++ unRar +++");

        try (RarArchive archive = new RarArchive(newMod.toString())) {
            archive.extractToDirectory(newFolder.toString());
            log.trace("[unRar: aspose] --- unRar ---");
            return true;
        } catch (Exception e) {
            log.error("[unRar: aspose] cannot unRar", e);
        }

        return false;
    }

}
