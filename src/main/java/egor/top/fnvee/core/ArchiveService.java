package egor.top.fnvee.core;

import lombok.extern.slf4j.Slf4j;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ArchiveService {

    public static final String RANDOM_ACCESS_FILE_MODE_READ = "r";

    public boolean extract(Path newMod, Path newFolder) {
        if (ObjectUtils.anyNull(newMod, newFolder)) {
            return false;
        }
        var map = extractToMap(newMod.toString());
        try {
            for (var entry : map.entrySet()) {
                final var path = entry.getKey();
                final var is = entry.getValue();

                Path newFile = Paths.get(newFolder.toString(), path);
                FileUtils.createParentDirectories(newFile.toFile());
                Files.createFile(newFile);

                var fos = new FileOutputStream(newFile.toFile());
                is.transferTo(fos);
                fos.flush();
                is.close();
                fos.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Map<String, InputStream> extractToMap(String filePath) {
        Map<String, InputStream> map = new HashMap<>();

        try (
                RandomAccessFileInStream rafis = new RandomAccessFileInStream(new RandomAccessFile(filePath, RANDOM_ACCESS_FILE_MODE_READ));
                IInArchive archive = SevenZip.openInArchive(null, rafis)
        ) {
            for (ISimpleInArchiveItem archiveItem : archive.getSimpleInterface().getArchiveItems()) {
                if (archiveItem.isFolder()) continue;

                ExtractOperationResult result = archiveItem.extractSlow(bytes -> {
                    map.put(archiveItem.getPath(), new BufferedInputStream(new ByteArrayInputStream(bytes)));
                    return bytes.length;
                });

                if (!ExtractOperationResult.OK.equals(result)) {
                    throw new RuntimeException(String.format("Error extracting archive. Extracting error: %s", result));
                }
            }
        } catch (Exception e) {
            log.error("[extractToMap] error", e);
            map.clear();
        }

        return map;
    }
}
