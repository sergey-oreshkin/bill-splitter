package home.serg.billsplitter.dataio;

import home.serg.billsplitter.model.BillMatrix;
import home.serg.billsplitter.parser.BillDataParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BillDataReaderFromFile implements BillDataReader {
    
    BillDataParser parser;
    
    public BillDataReaderFromFile(BillDataParser parser) {
        this.parser = parser;
    }
    
    @Override
    public BillMatrix getInputMatrix(String path) throws IOException {
        try {
            return parser.getExpenseMatrix(Files.readString(Path.of(path)));
        } catch (IOException ex) {
            throw new IOException("Не удалось прочитать данные из файла! Не верный путь или имя файла!", ex);
        }
    }
}
