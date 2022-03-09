import home.serg.billsplitter.BillSplitter;

public class Main {
    public static void main(String[] args) {
        String path = "resourses/input1.csv";
        
        if (args.length == 1) {
            path = args[0];
        }
        new BillSplitter().run(path);
    }
}
