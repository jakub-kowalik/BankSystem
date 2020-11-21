package banking;

import java.util.Scanner;

public class Controller {
    View view;
    Database database;
    Scanner scanner;

    public Controller(View view, Database database) {
        this.database = database;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        while (true) {
            view.showMenu();
            switch (scanner.nextInt()) {
                case 1:
                    createNewAccount();
                    break;
                case 2:
                    logInAccount();
                    break;
                case 0:
                    System.exit(0);
                case 100:
                    database.printTableContent();
                    break;
                default:
                    view.showError();
                    break;
            }
        }
    }

    public void createNewAccount() {
        Card card = new Card();

        database.addAccount(card);

        view.showAccountInformation(card);
    }


    public void logInAccount() {
        view.showCardNumberPrompt();
        String accountNumber = scanner.next();
        view.showCardPinPrompt();
        String pinCode = scanner.next();

        Card card = new Card(accountNumber, pinCode);

        if (database.isAccountPresent(card)) {
            view.showLoginStatus(true);
            accountMenu(card);
            return;
        }
        view.showLoginStatus(false);
    }

    public void accountMenu(Card card) {
        boolean flag = true;
        while (flag) {
            view.showAccountMenu();
            switch (scanner.nextInt()) {
                case 1:
                    view.showBalance(database.getAccountBalance(card));
                    break;
                case 2:
                    addBalance(card);
                    break;
                case 3:
                    doTransfer(card);
                    break;
                case 4:
                    deleteAccount(card);
                    flag = false;
                    break;
                case 5:
                    flag = false;
                    break;
                case 0:
                    System.exit(0);
                default:
                    view.showError();
                    break;
            }
        }
    }

    private void deleteAccount(Card card) {
        database.deleteAccount(card);
        view.showAccountClosed();
    }

    private void doTransfer(Card card) {
        view.showTransfer();
        view.showCardNumberPrompt();
        String transferFrom = scanner.next();
        if (!Utilities.checkLuhnCorrect(transferFrom)) {
            view.showInputNotValid();
            return;
        }
        if (!database.isAccountPresent(transferFrom)) {
            view.showCardNotExist();
            return;
        }
        view.showTransferValuePrompt();
        int targetTo = scanner.nextInt();
        if (database.getAccountBalance(card) >= targetTo) {
            database.transferBalance(card, transferFrom, targetTo);
            view.showTransferStatus(true);
        } else {
            view.showTransferStatus(false);
        }
    }

    private void addBalance(Card card) {
        view.showIncomePrompt();
        int accountNumberToCheck = scanner.nextInt();
        database.addBalance(card, accountNumberToCheck);
    }
}
