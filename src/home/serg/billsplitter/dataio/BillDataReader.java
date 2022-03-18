package home.serg.billsplitter.dataio;

import home.serg.billsplitter.model.BillMatrix;

import java.io.IOException;

public interface BillDataReader {
    
    BillMatrix getInputMatrix(String path) throws IOException;
}
