package api;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IFileHandler {

    List<File> extractTxtFiles(File directory);

    void mergeTxtFile(File firstFile, File secondFile) throws IOException;

}
