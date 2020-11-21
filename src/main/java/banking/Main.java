package banking;

public class Main {

    public static void main(String[] args) {
        String fileName = null;

        for (int i = 0; i < args.length; i++) {
            if ("-fileName".equals(args[i])) {
                fileName = args[i + 1];
            }
        }

        if (fileName == null) {
            System.out.println("No database specified - using default.s3db file");
            System.out.println("If you wish to use another file specify it with -fileName [argument]");
            fileName = "default.s3db";
        }

        Database database = new Database(fileName);

        View view = new View();
        Controller controller = new Controller(view, database);

        controller.menu();
    }
}