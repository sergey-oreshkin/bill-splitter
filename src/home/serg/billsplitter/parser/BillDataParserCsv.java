package home.serg.billsplitter.parser;

import home.serg.billsplitter.exception.ParseException;
import home.serg.billsplitter.model.BillMatrix;

import java.util.ArrayList;
import java.util.List;

public class BillDataParserCsv implements BillDataParser {
    
    int accuracy;
    
    public BillDataParserCsv() {
        accuracy = 100;
    }
    
    public BillDataParserCsv(int decimalPlaces) {
        this.accuracy = (int) Math.pow(10, decimalPlaces);
    }
    
    @Override
    public BillMatrix getExpenseMatrix(String content) throws ParseException {
        
        String splitter = content.contains("\r\n") ? "\r\n" : "\n";
        String[] lines = content.split(splitter);
        String[] header = lines[0].split(",");
        
        if (lines.length < 2)
            throw new ParseException("В файле нет записей!");
        
        if (header.length < 3)
            throw new ParseException("Нет ни одного имени в заголовке, или заголовок составлен не верно!");
        
        List<String> members = getMembers(header);
        
        return new BillMatrix(members, getMatrix(lines, members));
    }
    
    @Override
    public String[][] getOutputMatrix(BillMatrix billMatrix) {
        
        List<String> members = billMatrix.getMembers();
        int[][] matrix = billMatrix.getMatrix();
        String[][] outputMatrix = new String[members.size() + 1][members.size() + 1];
        String comma = ",";
        int size = members.size();
        outputMatrix[0][0] = "";
        
        // Fill first row and first column with names
        for (String name : members) {
            outputMatrix[members.indexOf(name) + 1][0] = name + comma;
            if (members.indexOf(name) == size - 1) comma = "";
            outputMatrix[0][members.indexOf(name) + 1] = name + comma;
            comma = ",";
        }
        
        // Fill the matrix with values
        for (int i = 1; i < size + 1; i++) {
            for (int j = 1; j < size + 1; j++) {
                if (j == size) comma = "";
                if (matrix[i - 1][j - 1] == 0) {
                    outputMatrix[i][j] = "0" + comma;
                    continue;
                }
                double value = (double) matrix[i - 1][j - 1] / accuracy;
                outputMatrix[i][j] = value + comma;
            }
            comma = ",";
        }
        return outputMatrix;
    }
    
    private List<String> getMembers(String[] header) throws ParseException {
        List<String> members = new ArrayList<>();
        
        for (int i = 2; i < header.length; i++) {
            if (members.contains(header[i])) {
                throw new ParseException("В первой строке должны быть указаны все участники без повторов!");
            }
            members.add(header[i]);
        }
        return members;
    }
    
    private int[][] getMatrix(String[] lines, List<String> members) {
        int[][] matrix = new int[members.size()][members.size()];
        
        for (int i = 1; i < lines.length; i++) {
            String[] words = lines[i].split(",");
            String name = words[0];
            
            for (int j = 2; j < words.length; j++) {
                if ("".equals(words[j])) continue;
                
                try {
                    double expense = Double.parseDouble(words[j]);
                    matrix[members.indexOf(name)][j - 2] += expense * accuracy;
                } catch (NumberFormatException ex) {
                    throw new ParseException(
                        "Некорректное значение!" +
                            "\nСтрока " + (i + 1) +
                            "\nСтолбец " + (j + 1) +
                            "\nЗначение " + words[j], ex);
                } catch (NullPointerException ex) {
                    throw new ParseException(
                        "Неизвестное имя участника!" +
                            "\nСтрока " + (i + 1) +
                            "\nИмя " + name);
                }
            }
        }
        return matrix;
    }
}