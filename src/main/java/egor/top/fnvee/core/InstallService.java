package egor.top.fnvee.core;

import egor.top.fnvee.core.un.UnService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Set;

@Slf4j
@Service
public class InstallService {

    @Autowired
    private Set<UnService> unServices;

    public boolean install(Path newMod, Path newFolder) {
        if (ObjectUtils.anyNull(newMod, newFolder)) {
            return false;
        }
        return IterableUtils.matchesAny(unServices, unService -> unService.extractAndCopyToFolder(newMod, newFolder));
    }
}
