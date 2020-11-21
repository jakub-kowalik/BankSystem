package banking;

public class View {

    void showMenu() {
        System.out.println("1. Create an account\n" +
                           "2. Log into account\n" +
                           "0. Exit");
    }

    void showAccountMenu() {
        System.out.println("1. Balance\n" +
                           "2. Add income\n" +
                           "3. Do transfer\n" +
                           "4. Close account\n" +
                           "5. Log out\n" +
                           "0. Exit");
    }

    void showAccountInformation(Card card) {
        System.out.println("Your card number:\n" + card.getCardNumber());
        System.out.println("Your card PIN:\n" + card.getPinNumber());
    }

    void showBalance(int balance) {
        System.out.println("Balance: " + balance);
    }

    void showLoginStatus(boolean status) {
        if (status) {
            System.out.println("You have successfully logged in!");
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }

    void showTransferStatus(boolean status) {
        if (status) {
            System.out.println("Success!");
        } else {
            System.out.println("Not enough money!");
        }
    }

    void showCardNumberPrompt() {
        System.out.println("Enter card number:");
    }

    void showCardPinPrompt() {
        System.out.println("Enter your PIN:");
    }

    void showError() {
        System.out.println("ERROR");
    }

    void showAccountClosed() {
        System.out.println("The account has been closed!");
    }

    void showTransferValuePrompt() {
        System.out.println("Enter how much money you want to transfer:");
    }

    void showCardNotExist() {
        System.out.println("Such a card does not exist.");
    }

    void showInputNotValid() {
        System.out.println("Probably you made mistake in the card number. Please try again!");
    }

    void showTransfer() {
        System.out.println("Transfer");
    }

    void showIncomePrompt() {
        System.out.println("Enter income:");
    }
}
