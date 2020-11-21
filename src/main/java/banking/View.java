package banking;

public class View {

    void showMenu() {
        System.out.println("""
                1. Create an account
                2. Log into account
                0. Exit""");
    }

    void showAccountMenu() {
        System.out.println("""
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit""");
    }

    void showAccountInformation(Card card) {
        System.out.println("Your card number:\n" + card.getCardNumber());
        System.out.println("Your card PIN:\n" + card.getPinNumber());
    }

}
