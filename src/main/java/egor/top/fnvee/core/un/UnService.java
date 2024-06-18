package egor.top.fnvee.core.un;

import egor.top.fnvee.core.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;

public abstract class UnService {

    @Autowired
    protected ArchiveService archiveService;

    public abstract boolean extractAndCopyToFolder(Path newMod, Path newFolder);
}
