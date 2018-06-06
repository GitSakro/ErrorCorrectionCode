package algorithm;

import utils.Utils;

import java.util.ArrayList;

import static utils.Utils.convertToBinaryString;
import static utils.Utils.searchNonDigit;

public class HammingCode implements Coder, Decoder {

    @Override
    public String encode(String message) {

        if(!searchNonDigit(message))
        {
            System.err.println("Message: " + message + " conatins non digit. Illegal form !! Aborting...");
            return message;
        }
        message = convertToBinaryString(message);
        message = new StringBuilder(message).reverse().toString();

        int bitsNeeded = checkBitsNeeded(message, true);
        message = addHammingSigns(message);
        ArrayList<Integer> positiveBitsPosition = getPositiveBitsPosition(message);
        ArrayList<Integer> parityBits = getParityBits(positiveBitsPosition, bitsNeeded);

        for (int i = 0, powerOfTwo = 1; i < bitsNeeded; ++i, powerOfTwo *= 2) {
            message = message.substring(0, powerOfTwo - 1) + parityBits.get(i) + message.substring(powerOfTwo, message.length());
        }

        return new StringBuilder(message).reverse().toString();
    }

    @Override
    public String decode(String message) {
        if (!Utils.searchNonDigit(message)) {
            System.err.println("Message corupted");
            return null;
        }
        message = new StringBuilder(message).reverse().toString();

        int bitsNeeded = checkBitsNeeded(message, false);
        ArrayList<Integer> positiveBitsPosition = getPositiveBitsPosition(message);
        ArrayList<Integer> parityBits = getParityBits(positiveBitsPosition, bitsNeeded);

        if (needRepair(parityBits)) {
            StringBuilder binaryNumber = new StringBuilder();
            for (int bit : parityBits) {
                binaryNumber.append(bit);
            }
            int binaryPosition = Integer.parseInt(binaryNumber.toString(), 2);
            message = message.substring(0, binaryPosition - 1) + (message.charAt(binaryPosition - 1) == '1' ? 0 : 1) + message.substring(binaryPosition, message.length());
        }
        StringBuilder result = new StringBuilder();
        int lastIndex = 0;
        for (int i = 1; i < message.length(); i *= 2) {
            result.append(message.substring(lastIndex, i - 1));
            lastIndex = i;
        }
        result.append(message.substring(lastIndex, message.length()));

        return new StringBuilder(result.toString()).reverse().toString();
    }

    private int checkBitsNeeded(String message, boolean addLength) {
        int bitsNeeded = 0;
        int length = message.length();
        for (int i = 1; i <= length; i *= 2) {
            bitsNeeded++;
            if (addLength) length++;
        }
        return bitsNeeded;
    }

    private ArrayList<Integer> getPositiveBitsPosition(String message) {
        ArrayList<Integer> positiveBitsPosition = new ArrayList<>();
        int position = 1;
        for (char bit : message.toCharArray()) {
            if (bit == '1') {
                positiveBitsPosition.add(position);
            }
            position++;
        }
        return positiveBitsPosition;
    }

    private ArrayList<Integer> getParityBits(ArrayList<Integer> positiveBitsPosition, int bitsNeeded) {
        ArrayList<Integer> parityBits = new ArrayList<>();
        for (Integer positive : positiveBitsPosition) {
            for (int bit = 1, i = 0; i < bitsNeeded; i++, bit <<= 1) {
                if (parityBits.size() - 1 < i) parityBits.add(0);
                parityBits.set(i, parityBits.get(i) + ((positive & bit) > 0 ? 1 : 0));
            }
        }

        for (int i = 0; i < parityBits.size(); ++i) {
            parityBits.set(i, parityBits.get(i) % 2 == 0 ? 0 : 1);
        }

        return parityBits;
    }

    private String addHammingSigns(String message) {
        for (int i = 1; i < message.length(); i = i * 2) {
            message = message.substring(0, i - 1) + "H" + message.substring(i - 1, message.length());
        }
        return message;
    }

    private boolean needRepair(ArrayList<Integer> parityBits) {
        for (int i : parityBits) {
            if (i == 1) return true;
        }
        return false;
    }
}