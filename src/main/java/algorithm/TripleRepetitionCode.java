package algorithm;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class TripleRepetitionCode implements Coder, Decoder {
    private static final String correctTriplePattern = "0{3}|1{3}";
    private static final String nonABinary = "[^01]";
    @Override
    public String encode(String message) {
        if(!searchNonDigit(message))
        {
            System.err.println("Message: " + message + " conatins non digit. Illegal form !! Aborting...");
            return null;
        }
        message = convertToBinaryString(message);
        return tripleEachBit(message);
    }
    private Boolean searchNonDigit(String message){
        return !Pattern.compile("\\D").matcher(message).find();
    }
    private String convertToBinaryString(String message){
        String returnMessage = message;
        if(Pattern.compile(nonABinary).matcher(message).find()){
            returnMessage = Integer.toBinaryString(Integer.parseInt(returnMessage));
        }
        return returnMessage;
    }
    private String tripleEachBit(String message){
        StringBuilder codedMessage = new StringBuilder();
        for (char bit:message.toCharArray()) {
            codedMessage.append(StringUtils.repeat(Character.toString(bit),3));
        }
        return codedMessage.toString();
    }
    private char decodeTriples(String tripleValue) {
        if (Pattern.compile(correctTriplePattern).matcher(tripleValue).find()) {
            return tripleValue.charAt(0);
        }
        int countZero = 0, countOne = 0;
        for (char bit : tripleValue.toCharArray()) {
            if (bit == '0' ) {
                countZero++;
                continue;
            }
            countOne++;
        }
        if (countZero > countOne) {
            return '0';
        }
        return '1';
    }
    @Override
    public String decode(String message) {
        if(message.length()%3 != 0 && !searchNonDigit(message)){
            System.err.println("Message corupted");
            return null;
        }
        StringBuilder decodedMessage = new StringBuilder();
        for(int i = 0; i<message.length();i+=3){
            decodedMessage.append(decodeTriples(""+message.charAt(i)+message.charAt(i+1)+message.charAt(i+1)));
        }

        return decodedMessage.toString();
    }
}
