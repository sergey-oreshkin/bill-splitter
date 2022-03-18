package home.serg.billsplitter.model;

import java.math.BigDecimal;
import java.util.List;

public class BillMatrix {
    
    private BigDecimal[][] matrix;
    private List<String> members;
    
    public BillMatrix(List<String> members, BigDecimal[][] matrix) {
        this.matrix = matrix;
        this.members = members;
    }
    
    public BigDecimal[][] getMatrix() {
        return matrix;
    }
    
    public List<String> getMembers() {
        return members;
    }
}
