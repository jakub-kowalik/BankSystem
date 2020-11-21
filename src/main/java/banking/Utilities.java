package banking;

import java.util.Random;

public class Utilities {

    static String cardPre = "4000000";

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
        int x;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            x = Integer.parseInt(number.substring(i, i + 1));

            if (alternate) {
                x *= 2;
                if (x > 9) {
                    x -= 9;
                }
            }

            sum += x;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public static int luhnAlgorithmImplementation(String number) {
        int sum = 0;
        int x;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            x = Integer.parseInt(number.substring(i, i + 1));

            if (alternate) {
                x *= 2;
                if (x > 9) {
                    x -= 9;
                }
            }

            sum += x;
            alternate = !alternate;
        }

        if (sum % 10 == 0) {
            return 0;
        } else {
            return (10 - (sum % 10));
        }
    }
}
