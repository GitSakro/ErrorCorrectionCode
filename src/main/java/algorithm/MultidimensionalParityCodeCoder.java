package algorithm;

import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MultidimensionalParityCodeCoder implements Coder, Decoder {
    private static final int MARGIN = 4;

    private enum Type {ROW, COLUMN;}

    ;

    @Override
    public String encode(String message) {
        if (message == null || message.length() == 0) {
//            throw new EmptyMessageException();
            return null;
        }

//        message = Utils.convertToBinaryString(message);

        int len = message.length();
        int matrixRowLength = (int) Math.ceil(Math.sqrt(len));

        char[] messageArray = message.toCharArray();

        char[][] matrix = new char[matrixRowLength + MARGIN][matrixRowLength + MARGIN];

        for (int i = 0; i < len; i += matrixRowLength) {
            int k = i;
            do {
                matrix[i / matrixRowLength][k % matrixRowLength] = messageArray[k];
                ++k;
            }
            while (k < len && k % matrixRowLength != 0);

            int rowSum = accumulateRow(matrix[i / matrixRowLength]);
            insertRowSum(rowSum, matrix[i / matrixRowLength], matrixRowLength);
        }

        for (int column = 0; column < matrixRowLength; ++column) {
            int columnSum = getColumnSum(matrix, column);
            insertColumnSum(columnSum, matrix, matrixRowLength, column);
        }

        return matrixToString(matrix);
    }

    private int accumulateRow(char[] matrixRow) {
        int sum = 0;
        for (char c : matrixRow) {
            if (Utils.isCharEmpty(c)) {
                break;
            }
            sum += Utils.charToInt(c);
        }

        return sum;
    }

    private void insertRowSum(int rowSum, char[] matrix, int matrixRowLength) {
        char[] sum = Utils.intToCharArray(rowSum);
        if (sum.length > MARGIN) {
            System.err.println("DANGEROUS!!!");
        }

        for (int i = 0; i < sum.length && i + matrixRowLength < matrix.length; ++i) {
            matrix[matrixRowLength + i] = sum[i];
        }
    }

    private int getColumnSum(char[][] matrix, int column) {
        int sum = 0;
        for (char[] chArr : matrix) {
            if (column >= chArr.length) {
                System.err.println("DANGEROUSSS");
            }

            if (Utils.isCharEmpty(chArr[column])) {
                break;
            }
            sum += Utils.charToInt(chArr[column]);
        }

        return sum;
    }

    private void insertColumnSum(int columnSum, char[][] matrix, int matrixRowLength, int column) {
        char[] sum = Utils.intToCharArray(columnSum);
        if (sum.length > MARGIN) {
            System.err.println("DANGEROUS!!!");
        }

        for (int i = 0; i < sum.length && i + matrixRowLength < matrix.length; ++i) {
            matrix[matrixRowLength + i][column] = sum[i];
        }
    }

    // for debug purposes only
    private void printMatrix(char[][] matrix) {
        System.out.println("Matrix [" + matrix.length + "][" + matrix[0].length + "]");
        System.out.println("______");
        for (char[] carr : matrix) {
            for (char ch : carr) {
                System.out.print(ch + " ");
            }
            System.out.println();
        }
        System.out.println("______");
    }

    private String matrixToString(char[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (char[] matrixRow : matrix) {
            for (char ch : matrixRow) {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    @Override
    public String decode(String message) {
        int contentLength = getEncodedMessageLength(message);

        char[][] matrix = new char[contentLength][contentLength];

        List<Integer> rowSums = new ArrayList<>();
        List<Integer> columnSums = new ArrayList<>();

        int count = 0;
        for (int i = 0; count < contentLength && i + contentLength < message.length(); i += contentLength + MARGIN, ++count) {
            char[] content = message.substring(i, i + contentLength).toCharArray();
            matrix[count] = content;

            String rowContent = message.substring(i + contentLength, i + contentLength + MARGIN).trim();
            rowSums.add(Integer.parseInt(rowContent));
        }

        for (int i = (contentLength + MARGIN) * contentLength; i < (contentLength + MARGIN) * contentLength + contentLength; ++i) {
            int j = i;
            StringBuilder builder = new StringBuilder();
            while (j < message.length()) {
                builder.append(message.charAt(j));
                j += (contentLength + MARGIN);
            }

            columnSums.add(Integer.parseInt(builder.toString().trim()));
        }

        List<Integer> invalidRows = findInvalidRowOrColumn(Type.ROW, matrix, rowSums);
        List<Integer> invalidColumns = findInvalidRowOrColumn(Type.COLUMN, matrix, columnSums);

        if (invalidColumns.size() == 1 && invalidRows.size() == 1) {
            fixMatrix(matrix, columnSums, rowSums, invalidColumns.get(0), invalidRows.get(0));
        } else if (invalidColumns.size() > 1 || invalidRows.size() > 1) {
            // throw exception???
        }

        return matrixToString(matrix);
    }

    private int getEncodedMessageLength(String message) {
        return ((int) Math.sqrt((double) message.length()) - MARGIN);
    }

    private List<Integer> findInvalidRowOrColumn(Type type, char[][] matrix, List<Integer> sumsInRowOrColumn) {
        List<Integer> invalid = new ArrayList<>();

        for (int i = 0; i < matrix.length; ++i) {
            int sum = 0;

            for (int j = 0; j < matrix[i].length; ++j) {
                char charToCheck = type == Type.ROW ? matrix[i][j] : matrix[j][i];
                if (Utils.isCharEmpty(charToCheck))
                    break;
                sum += Utils.charToInt(charToCheck);
            }

            if (sum != sumsInRowOrColumn.get(i)) {
                invalid.add(i);
            }
        }

        return invalid;
    }

    private void fixMatrix(char[][] matrix, List<Integer> columnSums, List<Integer> rowSums, int invalidColumn, int invalidRow) {
        int rowSum = accumulateRow(matrix[invalidRow]);
        int validValue = rowSums.get(invalidRow) - (rowSum - Utils.charToInt(matrix[invalidRow][invalidColumn]));

        matrix[invalidRow][invalidColumn] = Utils.intToCharArray(validValue)[0];
    }

    public static void main(String[] args) {
        MultidimensionalParityCodeCoder coder = new MultidimensionalParityCodeCoder();

        String input = "12345678";

        String encoded = coder.encode(input);
        System.out.println(encoded);

        String decoded = coder.decode(encoded);
        System.out.println(decoded);
        decoded = "1436   45615  78 15  119    25                   ";
//        decoded = "2236   45615  78 15  119    25                   ";
        System.out.println(coder.decode(decoded));
    }
}
