package egor.top.fnvee.core.un;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static egor.top.fnvee.core.PathService._zip;

@Slf4j
@Service
public class UnZipService extends UnService {

    @Override
    public boolean extractAndCopyToFolder(Path newMod, Path newFolder) {
        if (ObjectUtils.anyNull(newMod, newFolder) || !StringUtils.endsWithIgnoreCase(newMod.toString(), _zip)) {
            return false;
        }

        try {
            ZipFile zipFile = new ZipFile(newMod.toFile());
            log.trace("[unZip] +++ unZip +++");
            zipFile.extractAll(newFolder.toString());
            log.trace("[unZip] --- unZip ---");
            return true;
        } catch (ZipException e) {
            log.error("[unZip] cannot unZip", e);
            return false;
        }
    }

}
