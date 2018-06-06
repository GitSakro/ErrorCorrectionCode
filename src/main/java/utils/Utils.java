package utils;

import java.util.regex.Pattern;

public class Utils {
    private static final String nonABinary = "[^01]";

    public static String convertToBinaryString(String message) {
        String returnMessage = message;
        if (Pattern.compile(nonABinary).matcher(message).find()) {
            returnMessage = Integer.toBinaryString(Integer.parseInt(returnMessage));
        }
        return returnMessage;
    }

    public static boolean isCharEmpty(char ch) {
        return ch == '\u0000' || ch == ' ';
    }

    public static char[] intToCharArray(int i) {
        return ("" + i).toCharArray();
    }

    public static int charToInt(char ch) {
        return Integer.parseInt("" + ch);
    }

    public static Boolean searchNonDigit(String message) {
        return !Pattern.compile("\\D").matcher(message).find();
    }
}
