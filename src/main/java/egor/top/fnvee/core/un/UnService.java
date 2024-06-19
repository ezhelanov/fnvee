package egor.top.fnvee.core.un;

import java.nio.file.Path;

public abstract class UnService {

    public abstract boolean extractAndCopyToFolder(Path newMod, Path newFolder);
}
