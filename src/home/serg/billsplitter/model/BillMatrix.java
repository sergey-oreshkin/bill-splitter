package home.serg.billsplitter.model;

import java.util.List;

public class BillMatrix {
    
    private int[][] matrix;
    private List<String> members;
    
    public BillMatrix(List<String> members, int[][] matrix) {
        this.matrix = matrix;
        this.members = members;
    }
    
    public int[][] getMatrix() {
        return matrix;
    }
    
    public List<String> getMembers() {
        return members;
    }
}
