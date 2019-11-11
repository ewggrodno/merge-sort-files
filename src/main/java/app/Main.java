package app;

import api.IFileHandler;
import api.ITempFileHandler;
import handler.FileHandler;
import handler.TempFileHandler;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        File directory = new File(args[0]);
        File resultFile = new File(directory.getPath(), args[1]);

        if (!resultFile.exists()) {
            IFileHandler fileHandler = new FileHandler();
            List<File> dataFiles = fileHandler.extractTxtFiles(directory);

            ITempFileHandler tempFileHandler = new TempFileHandler();
            for (File dataFile : dataFiles) {
                tempFileHandler.moveFileToTempTxtFiles(dataFile);
            }

            resultFile.createNewFile();
            List<File> tempFiles = tempFileHandler.getTempFiles();
            for (File file : tempFiles) {
                fileHandler.mergeTxtFile(resultFile, file);
            }

            tempFileHandler.removeTempFiles();
        }

    }

}
