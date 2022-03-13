package home.serg.billsplitter.dataio;

import java.io.IOException;

public interface BillDataWriter {
    
    void write(String path, String[][] matrix) throws IOException;
}
