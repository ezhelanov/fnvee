package egor.top.fnvee.core.un;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import static egor.top.fnvee.core.PathService._7z;

@Slf4j
@Service
public class Un7zService extends UnService {

    @Override
    public boolean extractAndCopyToFolder(Path newMod, Path newFolder) {
        if (ObjectUtils.anyNull(newMod, newFolder) || !StringUtils.endsWithIgnoreCase(newMod.toString(), _7z)) {
            return false;
        }

        try (SevenZFile sevenZFile = SevenZFile.builder().setPath(newMod).get()) {
            SevenZArchiveEntry entry;
            log.trace("[un7z] +++ un7z +++");
            while (Objects.nonNull(entry = sevenZFile.getNextEntry())) {
                if (entry.isDirectory()) {
                    log.trace("[un7z] skipping entry {}", Strings.dquote(entry.getName()));
                    continue;
                }
                log.trace("[un7z] entry {}", Strings.dquote(entry.getName()));

                File curfile = new File(newFolder.toFile(), entry.getName());
                File parent = curfile.getParentFile();

                if (!parent.exists()) parent.mkdirs();

                FileOutputStream fos = new FileOutputStream(curfile);
                byte[] bytes = new byte[(int) entry.getSize()];
                sevenZFile.read(bytes, 0, bytes.length);
                fos.write(bytes);
                fos.close();
            }
            log.trace("[un7z] --- un7z ---");
            return true;
        } catch (IOException e) {
            log.error("[un7z] cannot un7zip", e);
        }

        return false;
    }

}
