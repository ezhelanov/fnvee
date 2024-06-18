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
        try {
            var map = extract(newMod.toString());
            for (var entry : map.entrySet()) {
                Path newFile = Paths.get(newFolder.toString(), entry.getKey());
                FileUtils.createParentDirectories(newFile.toFile());
                Files.createFile(newFile);
                FileOutputStream fos = new FileOutputStream(newFile.toFile());
                entry.getValue().transferTo(fos);
                fos.flush();
                entry.getValue().close();
                fos.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Map<String, InputStream> extract(String filePath) throws IOException {
        Map<String, InputStream> extractedMap = new HashMap<>();

        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, RANDOM_ACCESS_FILE_MODE_READ);
        RandomAccessFileInStream randomAccessFileStream = new RandomAccessFileInStream(randomAccessFile);
        IInArchive inArchive = SevenZip.openInArchive(null, randomAccessFileStream);

        for (ISimpleInArchiveItem item : inArchive.getSimpleInterface().getArchiveItems()) {
            if (!item.isFolder()) {
                ExtractOperationResult result = item.extractSlow(data -> {
                    extractedMap.put(item.getPath(), new BufferedInputStream(new ByteArrayInputStream(data)));
                    return data.length;
                });

                if (result != ExtractOperationResult.OK) {
                    throw new RuntimeException(
                            String.format("Error extracting archive. Extracting error: %s", result));
                }
            }
        }

        return extractedMap;
    }
}
