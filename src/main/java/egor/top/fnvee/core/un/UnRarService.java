package egor.top.fnvee.core.un;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static egor.top.fnvee.core.PathService._rar;

@Slf4j
@Service
@Deprecated(forRemoval = true)
public class UnRarService extends UnService {

    @Override
    public boolean extractAndCopyToFolder(Path newMod, Path newFolder) {
        if (ObjectUtils.anyNull(newMod, newFolder) || !StringUtils.endsWithIgnoreCase(newMod.toString(), _rar)) {
            return false;
        }

        return false;
        //return archiveService.extract(newMod, newFolder);
    }

}
