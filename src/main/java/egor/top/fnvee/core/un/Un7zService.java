package egor.top.fnvee.core.un;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
public class Un7zService extends UnService {
    @Override
    public boolean extractAndCopyToFolder(Path newMod, Path newFolder) {
        return false;
    }
}
