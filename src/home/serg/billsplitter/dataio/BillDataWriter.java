package home.serg.billsplitter.dataio;

import home.serg.billsplitter.model.BillMatrix;

import java.io.IOException;

public interface BillDataWriter {
    
    void write(String path, BillMatrix matrix) throws IOException;
}
