package home.serg.billsplitter.parser;

import home.serg.billsplitter.exception.ParseException;
import home.serg.billsplitter.model.BillMatrix;

public interface BillDataParser {
    
    BillMatrix getExpenseMatrix(String content) throws ParseException;
    
    String[][] getOutputMatrix(BillMatrix matrix);
}
