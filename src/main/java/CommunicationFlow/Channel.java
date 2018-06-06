package CommunicationFlow;

import java.util.Random;

public class Channel {
    private String inMessage = "";

    public void startTransmision(String message) {
        inMessage = message;
    }

    public void negateRandomBit() {
        int bitToNegate = getRandomBit();
        System.out.println("Before negation: " + inMessage);
        negateBitAtPossition(bitToNegate);
        System.out.println("After negation: " + inMessage);
    }

    private void negateBitAtPossition(int possition) {
        inMessage = inMessage.substring(0, possition) + reverseBit(inMessage.charAt(possition)) + inMessage.substring(possition + 1);
    }

    private char reverseBit(char bit) {
        if (bit == '0')
            return '1';
        return '0';
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
