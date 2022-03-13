package home.serg.billsplitter.dataio;

import java.io.IOException;

public interface BillDataReader {
    
    String getContent(String path) throws IOException;
}
