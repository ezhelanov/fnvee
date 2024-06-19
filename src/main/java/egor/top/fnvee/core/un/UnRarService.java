package egor.top.fnvee.core.un;

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
            log.trace("[unRar] +++ unRar +++");
            ExtractArchive extractArchive = new ExtractArchive();
            extractArchive.extractArchive(newMod.toFile(), newFolder.toFile());
            log.trace("[unRar] --- unRar ---");
            return true;
        } catch (Exception e) {
            log.trace("[unRar] error", e);
        }

        return false;
    }

}
