package api;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ITempFileHandler {

    void removeTempFiles();

    File getNewTempFile();

    List<File> getTempFiles();

    void sortAndWriteTempTxtFile(List<String> rows) throws IOException;

    void moveFileToTempTxtFiles(File file) throws IOException;

}
