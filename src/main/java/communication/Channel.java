package communication;

import java.util.Random;

public class Channel {
    private String inMessage = "";

    public void startTransmission(String message) {
        inMessage = message;
    }

    public void negateRandomBit() {
        int bitToNegate = getRandomBit();
        System.out.println("Before negation: " + inMessage);
        negateBitAtPosition(bitToNegate);
        System.out.println("After negation: " + inMessage);
    }

    private void negateBitAtPosition(int position) {
        inMessage = inMessage.substring(0, position) + reverseBit(inMessage.charAt(position)) + inMessage.substring(position + 1);
    }

    private char reverseBit(char bit) {
        return bit == '0' ? '1' : '0';
    }

    public void negateRandomBits(int numberOfBits) {
        for (int i = 0; i < numberOfBits; i++) {
            negateRandomBit();
        }
    }

    public int numberOfBitsInChannel() {
        return inMessage.length();
    }

    private int getRandomBit() {
        Random rand = new Random();

        return rand.nextInt((inMessage.length()) + 1);
    }

    public String endTransmission() {
        return inMessage;
    }
}
