package finiteField;

public class FiniteField {

    private static FiniteField instance = null;

    private static final byte[] irreduciblePolynomials = new byte[]{0, 0, 0, 1, 1, 0, 1, 1};

    private static final int[][] multiplicationTable = new int[256][256];

    private static final int[][] divisionTable = new int[256][256];

    private static final int n = 8;

    private static final int p = 2;

    public static FiniteField getInstance() {
        if (instance == null) {
            instance = new FiniteField();
        }
        return instance;
    }

    private FiniteField() {
        int numElement = (int) Math.pow(p, n);
        for (int i = 0; i < numElement; i++) {
            for (int j = 0; j < numElement; j++) {
                multiplicationTable[i][j] = getValue(multiply(toBinary(i), toBinary(j), n));
                divisionTable[i][j] = getValue(divide(toBinary(i), toBinary(j), n));
            }
        }
    }

    private byte[] exclusiveOr(byte[] a, byte[] b) {
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

    private byte[] toBinary(int a) {
        String binaryString = Integer.toBinaryString(a);
        if (binaryString.length() > FiniteField.n) {
            throw new IllegalStateException("overflow occur");
        }

        byte[] binary = new byte[FiniteField.n];
        for (int i = binaryString.length() - 1, j = FiniteField.n - 1; i >= 0; i--, j--) {
            binary[j] = (byte) (binaryString.charAt(i) - '0');
        }
        return binary;
    }

    private int getValue(byte[] a) {
        String binaryString = "";
        for (byte b : a) {
            binaryString += b;
        }
        return Integer.parseInt(binaryString, 2);
    }

    public int add(int a, int b) {
        return a ^ b;
    }

    public int minus(int a, int b) {
        return a ^ b;
    }

    private byte[] multiply(byte[] a, byte[] b, int n) {
        //refer to page 25-26 of https://engineering.purdue.edu/kak/compsec/NewLectures/Lecture7.pdf
        if (a.length != b.length) {
            throw new IllegalStateException();
        }

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
                a = exclusiveOr(shiftLeftLogical(a), irreduciblePolynomials);
            }
        }
        return result;
    }

    public int multiply(int a, int b) {
        return multiplicationTable[a][b];
    }

    private byte[] power(byte[] a, int index, int n) {
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

    private byte[] divide(byte[] a, byte[] b, int n) {
        if (a.length != b.length) {
            throw new IllegalStateException();
        }
        // recall that a / b == a * inverseOf(b)
        // from wiki: the inverse of x is x^(p^n − 2).
        byte[] inverse = power(b, (int) (Math.pow(2, n) - 2), n);
        return multiply(a, inverse, n);
    }

    public int divide(int a, int b) {
        return divisionTable[a][b];
    }

}
