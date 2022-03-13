package home.serg.billsplitter.dataio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BillDataWriterToFile implements BillDataWriter {
    @Override
    public void write(String path, String[][] matrix) throws IOException {
        try (BufferedWriter outputWriter = new BufferedWriter(new FileWriter(path))) {
            for (String[] str : matrix) {
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
