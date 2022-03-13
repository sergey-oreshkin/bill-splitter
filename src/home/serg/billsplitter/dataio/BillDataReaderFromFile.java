package home.serg.billsplitter.dataio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BillDataReaderFromFile implements BillDataReader {
    @Override
    public String getContent(String path) throws IOException {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException ex) {
            throw new IOException("Не удалось прочитать данные из файла! Не верный путь или имя файла!", ex);
        }
    }
}
