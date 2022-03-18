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
    
    public static void main(String[] args) {
        
        setParameters(args);
        
        BillDataReader reader = new BillDataReaderFromFile();
        BillDataParser parser = new BillDataParserCsv();
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
        inputPath = "resourses/input1.csv";
        outputPath = "output.csv";
        switch (params.length) {
            case 2:
                outputPath = params[1];
            case 1:
                inputPath = params[0];
        }
        
        System.out.println(
            "Применены следующие значения" +
                "\n Входной файл - " + inputPath +
                "\n Выходной файл - " + outputPath
        );
    }
}