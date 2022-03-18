package home.serg.billsplitter.dataio;

import home.serg.billsplitter.model.BillMatrix;
import home.serg.billsplitter.parser.BillDataParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BillDataWriterToFile implements BillDataWriter {
    
    BillDataParser parser;
    
    public BillDataWriterToFile(BillDataParser parser) {
        this.parser = parser;
    }
    
    @Override
    public void write(String path, BillMatrix matrix) throws IOException {
        
        try (BufferedWriter outputWriter = new BufferedWriter(new FileWriter(path))) {
            for (String[] str : parser.getOutputMatrix(matrix)) {
                for (String s : str) {
                    outputWriter.write(s);
                }
                outputWriter.newLine();
            }
            outputWriter.flush();
        } catch (IOException e) {
            throw new IOException("Не удалось записать в файл!", e);
        }
    }
}
