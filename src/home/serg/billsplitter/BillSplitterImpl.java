package home.serg.billsplitter;

import home.serg.billsplitter.exception.SplitException;
import home.serg.billsplitter.model.BillMatrix;
import home.serg.billsplitter.model.BillTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillSplitterImpl implements BillSplitter {
    
    @Override
    public BillMatrix getOutputMatrix(BillMatrix billMatrix) throws SplitException {
        
        int[][] matrix = billMatrix.getMatrix();
        List<String> members = billMatrix.getMembers();
        int[] delta = getDelta(matrix, members);
        int balance = Arrays.stream(delta).sum();
        
        if (balance != 0) throw new SplitException("Баланс трат не сходится! Перепроверьте данные.");
        
        List<BillTransaction> transactions = getTransactions(delta, members);
        
        return new BillMatrix(members, getMatrix(transactions, members));
    }
    
    private int[][] getMatrix(List<BillTransaction> transactions, List<String> members) {
        int[][] matrix = new int[members.size()][members.size()];
        
        for (BillTransaction t : transactions) {
            int i = members.indexOf(t.getDebtor());
            int j = members.indexOf(t.getCreditor());
            matrix[i][j] = t.getDebt();
        }
        return matrix;
    }
    
    private List<BillTransaction> getTransactions(int[] delta, List<String> members) {
        List<BillTransaction> transactions = new ArrayList<>();
        
        for (int i = 0; i < delta.length; i++) {
            if (delta[i] == 0) continue;
            
            if (delta[i] > 0) {
                for (int j = 0; j < delta.length; j++) {
                    if (i == j) continue;
                    
                    if (delta[j] < 0) {
                        if (delta[i] < Math.abs(delta[j])) {
                            transactions.add(new BillTransaction(members.get(j), members.get(i), Math.abs(delta[i])));
                            delta[j] += delta[i];
                            delta[i] = 0;
                        } else {
                            transactions.add(new BillTransaction(members.get(j), members.get(i), Math.abs(delta[j])));
                            delta[i] += delta[j];
                            delta[j] = 0;
                        }
                    }
                }
            }
        }
        return transactions;
    }
    
    private int[] getDelta(int[][] matrix, List<String> members) {
        int total = members.size();
        int[] delta = new int[total];
        
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                if (i == j) continue;
                delta[i] -= matrix[i][j];
                delta[i] += matrix[j][i];
            }
        }
        return delta;
    }
}