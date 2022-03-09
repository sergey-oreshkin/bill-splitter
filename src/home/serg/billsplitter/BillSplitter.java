package home.serg.billsplitter;

import home.serg.billsplitter.exception.ParseException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillSplitter {
    
    private static final String OUTPUT_FILE_NAME = "output.csv";
    
    private int total;
    private String[][] outputMatrix;
    private double[][] expensesMatrix;
    private double[] deltaMoney;
    private final Map<String, Integer> friendsId = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();
    
    public void run(String path) {
        try {
            parseExpenses(getFileContent(path));
            calculateDelta();
            mappingTransaction();
            printTransaction();
            makeOutputMatrix();
            saveOutputMatrix();
        } catch (IOException | RuntimeException e) {
            System.err.println("В работе программы произошла ошибка!");
            e.printStackTrace();
        }
    }
    
    private void saveOutputMatrix() throws IOException {
        try (BufferedWriter outputWriter = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {
            for (String[] str : outputMatrix) {
                for (String s : str) {
                    outputWriter.write(s);
                }
                outputWriter.newLine();
            }
            outputWriter.flush();
        } catch (IOException e) {
            throw new IOException("Не удалось записать в файл!", e);
        }
    }
    
    private void makeOutputMatrix() {
        outputMatrix = new String[total + 1][total + 1];
        String comma = ",";
        outputMatrix[0][0] = comma;
        
        // Fill first row and first column with names
        for (String name : friendsId.keySet()) {
            outputMatrix[friendsId.get(name) + 1][0] = name + comma;
            if (friendsId.get(name) == total - 1) comma = "";
            outputMatrix[0][friendsId.get(name) + 1] = name + comma;
            comma = ",";
        }
        
        // Fill the matrix with "0"
        for (int i = 1; i < total + 1; i++) {
            for (int j = 1; j < total + 1; j++) {
                if (j == total) comma = "";
                outputMatrix[i][j] = "0" + comma;
            }
            comma = ",";
        }
        
        // Replace "0" with transaction values
        for (Transaction t : transactions) {
            int i = friendsId.get(t.debtor) + 1;
            int j = friendsId.get(t.creditor) + 1;
            outputMatrix[i][j] = outputMatrix[i][j].replace("0", String.valueOf(t.debt));
        }
    }
    
    private void printTransaction() {
        if (transactions.isEmpty()) {
            System.out.println("Никто! Никому! Ничего! Не должен!");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println(t.debtor + " должен " + t.creditor + " " + t.debt + " у.е.");
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
                            transactions.add(new Transaction(getFriendsName(j),
                                                            getFriendsName(i),
                                                            Math.abs(deltaMoney[i] / 100)));
                            deltaMoney[j] += deltaMoney[i];
                            deltaMoney[i] = 0;
                        } else {
                            transactions.add(new Transaction(getFriendsName(j),
                                                            getFriendsName(i),
                                                            Math.abs(deltaMoney[j] / 100)));
                            deltaMoney[i] += deltaMoney[j];
                            deltaMoney[j] = 0;
                        }
                    }
                }
            }
        }
    }
    
    private String getFriendsName(int id) throws RuntimeException {
        for (String s : friendsId.keySet()) {
            if (friendsId.get(s) == id) return s;
        }
        throw new RuntimeException("Unknown Error!");
    }
    
    private void calculateDelta() {
        total = friendsId.size();
        deltaMoney = new double[total];
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                if (i == j) continue;
                deltaMoney[i] -= expensesMatrix[i][j];
                deltaMoney[i] += expensesMatrix[j][i];
            }
        }
    }
    
    private void parseExpenses(String content) {
        String splitter = content.contains("\r\n") ? "\r\n" : "\n";
        String[] lines = content.split(splitter);
        int totalLines = lines.length - 1;
        
        if (totalLines < 1) {
            throw new ParseException("В файле нет записей!");
        }
        
        // Get unique names from first line
        String[] header = lines[0].split(",");
        for (int i = 2; i < header.length; i++) {
            friendsId.put(header[i], i - 2);
        }
        
        expensesMatrix = new double[friendsId.size()][friendsId.size()];
        
        for (int i = 1; i < lines.length; i++) {
            String[] words = lines[i].split(",");
            String name = words[0];
            
            for (int j = 2; j < words.length; j++) {
                if ("".equals(words[j])) continue;
                double expense = Double.parseDouble(words[j]);
                expensesMatrix[friendsId.get(name)][j - 2] += expense * 100;
            }
            
        }
    }
    
    private String getFileContent(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
    
    static class Transaction {
        String creditor;
        String debtor;
        double debt;
        
        public Transaction(String creditor, String debtor, double debt) {
            this.creditor = creditor;
            this.debtor = debtor;
            this.debt = debt;
        }
    }
}
