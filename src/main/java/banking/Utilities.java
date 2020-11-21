package banking;

import java.util.Random;

public class Utilities {

    static final String cardPre = "4000000";

    public static String generateCardNumber() {
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder(cardPre);

        for (int i = 0; i < 8; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        stringBuilder.append(0);
        int checksum = luhnAlgorithmImplementation(stringBuilder.toString());
        stringBuilder.deleteCharAt(stringBuilder.length() - 1).append(checksum);

        return stringBuilder.toString();
    }

    public static String generatePin() {
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(random.nextInt(9) + 1);

        for (int i = 1; i < 4; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        return stringBuilder.toString();
    }

    public static boolean checkLuhnCorrect(String number) {
        int sum = 0;
        int digit;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            digit = Integer.parseInt(number.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static int luhnAlgorithmImplementation(String number) {
        int sum = 0;
        int digit;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            digit = Integer.parseInt(number.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        if (sum % 10 == 0) {
            return 0;
        } else {
            return (10 - (sum % 10));
        }
    }
}
