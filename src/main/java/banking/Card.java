package banking;

public class Card {
    private String cardNumber;
    private String pinNumber;

    public Card() {
        this.cardNumber = Utilities.generateCardNumber();
        this.pinNumber = Utilities.generatePin();
    }

    public Card(String cardNumber, String pinNumber) {
        this.cardNumber = cardNumber;
        this.pinNumber = pinNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinNumber() {
        return pinNumber;
    }

}
