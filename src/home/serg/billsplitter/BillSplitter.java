package home.serg.billsplitter;

import home.serg.billsplitter.exception.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BillSplitter {
    
    private int[][] expensesMatrix;
    private int[] deltaMoney;
    private final Map<String, Integer> friendsId = new HashMap<>();
    private final Map<String, Debtor> transaction = new HashMap<>();
    
    public void run(String path) {
        try {
            parseExpenses(getFileContent(path));
        } catch (ParseException | IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        calculateDelta();
        mappingTransaction();
        printTransaction();
    }
    
    private void printTransaction() {
        if (transaction.isEmpty()) {
            System.out.println("Никто! Никому! Ничего! Не должен!");
            return;
        }
        for (String receiver : transaction.keySet()) {
            Debtor debtor = transaction.get(receiver);
            System.out.println(debtor.name + " должен " + receiver + " " + debtor.debt + " у.е.");
        }
    }
    
    private void mappingTransaction() {
        for (int i = 0; i < deltaMoney.length; i++) {
            if (deltaMoney[i] == 0) continue;
            
            if (deltaMoney[i] > 0) {
                for (int j = 0; j < deltaMoney.length; j++) {
                    if (i == j) continue;
                    if (deltaMoney[j] < 0) {
                        if (deltaMoney[i] < Math.abs(deltaMoney[j])) {
                            transaction.put(getFriendsName(j),
                                new Debtor(getFriendsName(i), deltaMoney[i]));
                            deltaMoney[j] -= deltaMoney[i];
                            deltaMoney[i] = 0;
                        } else {
                            transaction.put(getFriendsName(j),
                                new Debtor(getFriendsName(i), deltaMoney[j]));
                            deltaMoney[i] -= deltaMoney[j];
                            deltaMoney[j] = 0;
                        }
                    }
                }
            }
        }
    }
    
    private String getFriendsName(int id) {
        for (String s : friendsId.keySet()) {
            if (friendsId.get(s) == id) return s;
        }
        System.err.println("Unknown Error!");
        return "";
    }
    
    private void calculateDelta() {
        int total = friendsId.size();
        deltaMoney = new int[total];
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                if (i == j) continue;
                deltaMoney[i] -= expensesMatrix[i][j];
                deltaMoney[i] += expensesMatrix[j][i];
            }
        }
    }
    
    private void parseExpenses(String content) throws ParseException {
        String splitter = content.contains("\r\n") ? "\r\n" : "\n";
        String[] lines = content.split(splitter);
        int totalLines = lines.length - 1;
        
        if (totalLines < 1) {
            throw new ParseException("В файле нет записей!");
        }
        
        expensesMatrix = new int[totalLines][totalLines];
        
        for (int i = 1; i < lines.length; i++) {
            String[] records = lines[i].split(",");
            String name = records[0];
            
            if (friendsId.containsKey(name)) {
                for (int j = 2; j < records.length; j++) {
                    if ("".equals(records[j])) continue;
                    int expense = Integer.parseInt(records[j]);
                    expensesMatrix[friendsId.get(name)][j - 2] = expense;
                }
            } else {
                friendsId.put(name, i - 1);
                for (int j = 2; j < records.length; j++) {
                    if ("".equals(records[j])) continue;
                    int expense = Integer.parseInt(records[j]);
                    expensesMatrix[i - 1][j - 2] += expense;
                }
            }
        }
    }
    
    private String getFileContent(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
    
    static class Debtor {
        String name;
        int debt;
        
        Debtor(String name, int debt) {
            this.name = name;
            this.debt = debt;
        }
    }
}
