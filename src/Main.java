import home.serg.billsplitter.BillSplitter;

public class Main {
    public static void main(String[] args) {
        String inputPath = "resourses/input.csv";
        String outputPath = "output.csv";
        
        if (args.length == 2) {
            inputPath = args[0];
            outputPath = args[1];
        } else if (args.length == 1) {
            inputPath = args[0];
        }
        
        new BillSplitter().run(inputPath, outputPath);
    }
}
