package home.serg.billsplitter;

import home.serg.billsplitter.exception.SplitException;
import home.serg.billsplitter.model.BillMatrix;

public interface BillSplitter {
    
    BillMatrix getOutputMatrix(BillMatrix matrix) throws SplitException;
}
