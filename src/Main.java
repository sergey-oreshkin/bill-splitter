import home.serg.billsplitter.BillSplitter;
import home.serg.billsplitter.BillSplitterImpl;
import home.serg.billsplitter.dataio.BillDataReader;
import home.serg.billsplitter.dataio.BillDataReaderFromFile;
import home.serg.billsplitter.dataio.BillDataWriter;
import home.serg.billsplitter.dataio.BillDataWriterToFile;
import home.serg.billsplitter.exception.ParseException;
import home.serg.billsplitter.exception.SplitException;
import home.serg.billsplitter.model.BillMatrix;
import home.serg.billsplitter.parser.BillDataParser;
import home.serg.billsplitter.parser.BillDataParserCsv;

import java.io.IOException;

public class Main {
    private static String inputPath = "";
    private static String outputPath = "";
    private static int decimalPlaces = 0;
    
    public static void main(String[] args) {
        
        setParameters(args);
        
        BillDataReader reader = new BillDataReaderFromFile();
        BillDataParser parser = new BillDataParserCsv(decimalPlaces);
        BillSplitter splitter = new BillSplitterImpl();
        BillDataWriter writer = new BillDataWriterToFile();
        
        try {
            BillMatrix billMatrix = parser.getExpenseMatrix(reader.getContent(inputPath));
            billMatrix = splitter.getOutputMatrix(billMatrix);
            writer.write(outputPath, parser.getOutputMatrix(billMatrix));
            System.out.println("Выходные данные успешно сохранены.");
        } catch (IOException | ParseException | SplitException ex) {
            System.err.println("В работе программы произошла ошибка!");
            System.err.println(ex.getMessage());
        }
        
    }
    
    private static void setParameters(String[] params) {
        switch (params.length) {
            case 3:
                try {
                    decimalPlaces = Integer.parseInt(params[2]);
                } catch (NumberFormatException ex) {
                    System.out.println("Не удалось распознать третий параметр! Будет использовано значение по умолчанию.");
                }
            case 2:
                outputPath = params[1];
            case 1:
                inputPath = params[0];
                break;
            default:
                inputPath = "resourses/input1.csv";
                outputPath = "output.csv";
                decimalPlaces = 2;
        }
        
        System.out.println(
            "Применены следующие значения" +
                "\n Точность - " + decimalPlaces + " знака после запятой" +
                "\n Входной файл - " + inputPath +
                "\n Выходной файл - " + outputPath
        );
    }
}