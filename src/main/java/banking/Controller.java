package banking;

import java.util.Scanner;

public class Controller {
    View view;
    Database database;

    public Controller(View view, Database database) {
        this.database = database;
        this.view = view;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            view.showMenu();
            switch (scanner.nextInt()) {
                case 1 -> createNewAccount();
                case 2 -> {
                    System.out.println("Enter your card number:");
                    String accountNumber = scanner.next();
                    System.out.println("Enter your PIN:");
                    String pinCode = scanner.next();
                    logInAccount(accountNumber, pinCode);
                }
                case 0 -> System.exit(0);
                case 100 -> database.readFromTable();
                default -> System.out.println("ERROR");
            }
        }
    }

    public void createNewAccount() {
        Card card = new Card();

        database.addRecordToTable(card);

        view.showAccountInformation(card);
    }


    public void logInAccount(String accountNumber, String pinCode) {
        Card card = new Card(accountNumber, pinCode);

        if (database.checkIfAccountExistInTable(card)) {
            System.out.println("You have successfully logged in!");
            accountMenu(card);
            return;
        }
        System.out.println("Wrong card number or PIN!");
    }

    public void accountMenu(Card card) {
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        while (flag) {
            view.showAccountMenu();
            //view.showAccountBalance(card);
            switch (scanner.nextInt()) {
                case 1 -> System.out.println("Balance: " + database.checkAccountBalance(card));
                case 2 -> {
                    System.out.println("Enter income:");
                    int accountNumberToCheck = scanner.nextInt();
                    database.addToBalance(card, accountNumberToCheck);
                }
                case 3 -> {
                    System.out.println("Transfer");
                    System.out.println("Enter card number:");
                    String transferFrom = scanner.next();
                    if (!Utilities.checkLuhnCorrect(transferFrom)) {
                        System.out.println("Probably you made mistake in the card number. Please try again!");
                        break;
                    }
                    if (!database.checkIfAccountExistInTable(transferFrom)) {
                        System.out.println("Such a card does not exist.");
                        break;
                    }
                    System.out.println("Enter how much money you want to transfer:");
                    int targetTo = scanner.nextInt();
                    if (database.checkAccountBalance(card) >= targetTo) {
                        database.transferBalance(card, transferFrom, targetTo);
                        System.out.println("Success!");
                    } else {
                        System.out.println("Not enough money!");
                    }
                }
                case 4 -> {
                    database.deleteRecordFromTable(card);
                    System.out.println("The account has been closed!");
                    flag = false;
                }
                case 5 -> flag = false;
                case 0 -> System.exit(0);
                default -> System.out.println("ERROR");
            }
        }
    }
}
