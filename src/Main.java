import home.serg.billsplitter.BillSplitter;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("В аргументах запуска должен быть передан только путь к файлу с данными!");
            return;
        }
        new BillSplitter().run(args[0]);
    }
}
