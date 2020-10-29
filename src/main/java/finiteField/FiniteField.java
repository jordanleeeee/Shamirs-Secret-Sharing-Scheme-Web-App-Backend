package finiteField;

import java.util.HashMap;

public class FiniteField {

    static final HashMap<Integer, byte[]> irreduciblePolynomials = new HashMap<>();

    static{
        irreduciblePolynomials.put(2, new byte[]{1, 1});
        irreduciblePolynomials.put(3, new byte[]{0, 1, 1});
        irreduciblePolynomials.put(4, new byte[]{0, 0, 1, 1});
        irreduciblePolynomials.put(5, new byte[]{0, 0, 1, 0, 1});
        irreduciblePolynomials.put(8, new byte[]{0, 0, 0, 1, 1, 0, 1, 1});
    }

    private static byte[] exclusiveOr(byte[] a, byte[] b) {
        if (a.length != b.length) {
            throw new IllegalStateException();
        }
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) ((a[i] == b[i]) ? 0 : 1);
        }
        return result;
    }

    // drop first bit, other bit shift left by 1 unit
    private static byte[] shiftLeftLogical(byte[] a) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length - 1; i++) {
            result[i] = a[i + 1];
        }
        return result;
    }

    private static byte[] toBinary(int a, int n) {
        String binaryString = Integer.toBinaryString(a);
        if (binaryString.length() > n) {
            throw new IllegalStateException("overflow occur");
        }

        byte[] binary = new byte[n];
        for (int i = binaryString.length() - 1, j = n - 1; i >= 0; i--, j--) {
            binary[j] = (byte) (binaryString.charAt(i) - '0');
        }
        return binary;
    }

    public static int getValue(byte[] a) {
        String binaryString = "";
        for (byte b : a) {
            binaryString += b;
        }
//        System.out.println(Arrays.toString(a) + "\t" + binaryString +
//                "\t" + Integer.parseInt(binaryString, 2));
        return Integer.parseInt(binaryString, 2);
    }

    public static int add(int a, int b) {
        return a ^ b;
    }

    public static int minus(int a, int b) {
        return a ^ b;
    }

    private static byte[] multiply(byte[] a, byte[] b, int n) {
        //refer to page 25-26 of https://engineering.purdue.edu/kak/compsec/NewLectures/Lecture7.pdf
        if (a.length != b.length) {
            throw new IllegalStateException();
        }
        byte[] m = irreduciblePolynomials.get(n);

        byte[] result = new byte[n];
        for (int i = n - 1; i >= 0; i--) {
            if (b[i] == 1) {
                // result += a
                result = exclusiveOr(result, a);
            }
            // perform a = a*x
            if (a[0] == 0) {
                a = shiftLeftLogical(a);
            } else {
                a = exclusiveOr(shiftLeftLogical(a), m);
            }
        }
        return result;
    }

    public static int multiply(int a, int b, int n) {
        return getValue(multiply(toBinary(a, n), toBinary(b, n), n));
    }

    private static byte[] power(byte[] a, int index, int n) {
        byte[] result = new byte[n];
        // deep copy first
        for (int i = 0; i < n; i++) {
            result[i] = a[i];
        }

        for (int i = 0; i < index - 1; i++) {
            result = multiply(result, a, n);
        }
        return result;
    }

    private static byte[] divide(byte[] a, byte[] b, int n) {
        if (a.length != b.length) {
            throw new IllegalStateException();
        }
        // recall that a / b == a * inverseOf(b)
        // from wiki: the inverse of x is x^(p^n ��� 2). idk why, don't ask me
        byte[] inverse = power(b, (int) (Math.pow(2, n) - 2), n);
        return multiply(a, inverse, n);
    }

    public static int divide(int a, int b, int n) {
        return getValue(divide(toBinary(a, n), toBinary(b, n), n));
    }

    //testing
    public static void main(String[] args) {
        int n = 4;
        for (int value = 0; value < 16; value++) {
            for (int value2 = 0; value2 < 16; value2++) {
                System.out.print(getValue(multiply(toBinary(value, n), toBinary(value2, n), n)) + "\t");
            }
            System.out.println();
        }

        System.out.println();

        for (int i = 0; i < 16; i++) {
            System.out.print(getValue(power(toBinary(i, n),14, n)) + "\t");
        }

        System.out.println("\n");

        for (int value = 0; value < 16; value++) {
            for (int value2 = 0; value2 < 16; value2++) {
                System.out.print(getValue(divide(toBinary(value, n), toBinary(value2, n), n)) + "\t");
            }
            System.out.println();
        }

    }
}
