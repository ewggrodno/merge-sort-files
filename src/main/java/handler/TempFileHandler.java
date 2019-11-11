package handler;

import api.ITempFileHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static constant.FileName.*;

public class TempFileHandler implements ITempFileHandler {

    private List<File> tempFiles;
    private int countTempFile = 0;

    public TempFileHandler() {
        this.tempFiles = new ArrayList<>();
    }

    @Override
    public List<File> getTempFiles() {
        return tempFiles;
    }

    @Override
    public File getNewTempFile() {
        File newTempFile = new File(StringUtils.join(TEMP_FILE_NAME, ++countTempFile, TXT));
        tempFiles.add(newTempFile);
        return newTempFile;
    }

    @Override
    public void removeTempFiles() {
        tempFiles.forEach(x -> x.delete());
        tempFiles.clear();
        countTempFile = 0;
    }

    @Override
    public void sortAndWriteTempTxtFile(List<String> rows) throws IOException {
        Collections.sort(rows);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getNewTempFile()))) {
            for (String row : rows) {
                writer.write(row);
                writer.newLine();
            }
        }
    }

    @Override
    public void moveFileToTempTxtFiles(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                List<String> tempRows = new ArrayList<>();
                for (int i = 0; i < PART_ROWS_SIZE && reader.ready(); i++) {
                    tempRows.add(reader.readLine());
                }
                sortAndWriteTempTxtFile(tempRows);
            }
        }
    }
}
