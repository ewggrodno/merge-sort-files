package handler;

import api.IFileHandler;
import constant.FileName;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler implements IFileHandler {

    private List<File> txtFiles;

    public FileHandler() {
        this.txtFiles = new ArrayList<>();
    }

    @Override
    public List<File> extractTxtFiles(File directory) {
        txtFiles.clear();
        File[] files = directory.listFiles();
        for (File file : files) {
            if (StringUtils.endsWith(file.getName(), FileName.TXT)) {
                txtFiles.add(file);
            }
        }
        return txtFiles;
    }

    @Override
    public void mergeTxtFile(File firstFile, File secondFile) throws IOException {
        File tempResult = new File(FileName.TEMP_RESULT_TXT);
        try (BufferedReader firstReader = new BufferedReader(new FileReader(firstFile));
            BufferedReader secondReader = new BufferedReader(new FileReader(secondFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempResult, true))) {

            boolean isFirstRead = true;
            boolean isSecondRead = true;
            String firstReaderRow = null;
            String secondReaderRow = null;
            while (firstReader.ready() || secondReader.ready()) {

                if (firstReader.ready() && isFirstRead) {
                    firstReaderRow = firstReader.readLine();
                }
                if (secondReader.ready() && isSecondRead) {
                    secondReaderRow = secondReader.readLine();
                }

                if (firstReaderRow == null || secondReaderRow == null) {
                    writeRow(writer, firstReaderRow, secondReaderRow);
                    isFirstRead = true;
                    isSecondRead = true;
                    firstReaderRow = null;
                    secondReaderRow = null;
                    continue;
                }

                int rowWeight = StringUtils.compare(firstReaderRow, secondReaderRow);
                if (rowWeight < 0) {
                    writeRow(writer, firstReaderRow);
                    firstReaderRow = null;
                    isFirstRead = true;
                    isSecondRead = false;
                } else if (rowWeight > 0) {
                    writeRow(writer, secondReaderRow);
                    secondReaderRow = null;
                    isSecondRead = true;
                    isFirstRead = false;
                } else {
                    writeRow(writer, firstReaderRow, secondReaderRow);
                    firstReaderRow = null;
                    secondReaderRow = null;
                    isFirstRead = true;
                    isSecondRead = true;
                }
            }
        }
        tempResult.renameTo(firstFile);
        tempResult.delete();
    }

    private void writeRow(BufferedWriter writer, String... rows) throws IOException {
        for (String row : rows) {
            if (row != null) {
            writer.write(row);
            writer.newLine();
            writer.flush();
            }
        }
    }

}
