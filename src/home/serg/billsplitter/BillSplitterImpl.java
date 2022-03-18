package home.serg.billsplitter;

import home.serg.billsplitter.exception.SplitException;
import home.serg.billsplitter.model.BillMatrix;
import home.serg.billsplitter.model.BillTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillSplitterImpl implements BillSplitter {
    
    @Override
    public BillMatrix getOutputMatrix(BillMatrix billMatrix) throws SplitException {
        
        BigDecimal[][] matrix = billMatrix.getMatrix();
        List<String> members = billMatrix.getMembers();
        BigDecimal[] delta = getDelta(matrix, members);
        BigDecimal balance = new BigDecimal(0);
        
        for (BigDecimal item : delta) {
            balance = balance.add(item);
        }
        
        if (balance.compareTo(new BigDecimal(0)) != 0)
            throw new SplitException("Баланс трат не сходится! Перепроверьте данные.");
        
        List<BillTransaction> transactions = getTransactions(delta, members);
        
        return new BillMatrix(members, getMatrix(transactions, members));
    }
    
    private BigDecimal[][] getMatrix(List<BillTransaction> transactions, List<String> members) {
        BigDecimal[][] matrix = new BigDecimal[members.size()][members.size()];
        for (BigDecimal[] row : matrix) {
            Arrays.fill(row, BigDecimal.ZERO);
        }
        
        for (BillTransaction t : transactions) {
            int i = members.indexOf(t.getDebtor());
            int j = members.indexOf(t.getCreditor());
            matrix[i][j] = new BigDecimal(t.getDebt());
        }
        return matrix;
    }
    
    private List<BillTransaction> getTransactions(BigDecimal[] delta, List<String> members) {
        List<BillTransaction> transactions = new ArrayList<>();
        
        for (int i = 0; i < delta.length; i++) {
            if (delta[i].compareTo(new BigDecimal(0)) == 0) continue;
            
            if (delta[i].compareTo(BigDecimal.ZERO) > 0) {
                for (int j = 0; j < delta.length; j++) {
                    if (i == j) continue;
                    
                    if (delta[j].compareTo(BigDecimal.ZERO) < 0) {
                        if (delta[i].doubleValue() < Math.abs(delta[j].doubleValue())) {
                            transactions.add(new BillTransaction(members.get(j), members.get(i), Math.abs(delta[i].doubleValue())));
                            delta[j] = delta[j].add(delta[i]);
                            delta[i] = BigDecimal.ZERO;
                        } else {
                            transactions.add(new BillTransaction(members.get(j), members.get(i), Math.abs(delta[j].doubleValue())));
                            delta[i] = delta[i].add(delta[j]);
                            delta[j] = BigDecimal.ZERO;
                        }
                    }
                }
            }
        }
        return transactions;
    }
    
    private BigDecimal[] getDelta(BigDecimal[][] matrix, List<String> members) {
        int total = members.size();
        BigDecimal[] delta = new BigDecimal[total];
        Arrays.fill(delta, BigDecimal.ZERO);
        
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                if (i == j) continue;
                delta[i] = delta[i].subtract(matrix[i][j]);
                delta[i] = delta[i].add(matrix[j][i]);
            }
        }
        return delta;
    }
}