package ssss;

import finiteField.FiniteField;

public class RecoverSecretPlus {

    private static final FiniteField finiteField = FiniteField.getInstance();
    private final String[] shares;
    private final int t;

	/**
     * @param shares share points
     * @param t      threshold to recover the secret
	 */
    public RecoverSecretPlus(String[] shares, int t) {
        if (t < 1) {
            throw new IllegalStateException();
        }
        this.shares = shares;
        this.t = t;
	}

    // refer to formula
    // https://en.wikipedia.org/wiki/Shamir%27s_Secret_Sharing#Computationally_efficient_approach
    @Deprecated
    public byte[] getSecret() {
        if (shares.length < t) {
            throw new IllegalStateException("not enough share to recover the secret");
        }

        int result = 0;
        int numOfByte = shares[0].split("-").length;
        byte[] secret = new byte[numOfByte - 1];// 1st is not share
        String[][] sharePerByte = new String[t][numOfByte];

        for (int i = 0; i < t; i++) {
            sharePerByte[i] = shares[i].split("-");
        }

        for (int k = 1; k < numOfByte; k++) {
            result = 0;
            for (int i = 0; i < t; i++) {
                int temp = Integer.parseInt(sharePerByte[i][k]); // temp = Y(i)
                for (int j = 0; j < t; j++) {
                    if (i != j) {
                        int fraction = finiteField.divide(Integer.parseInt(sharePerByte[j][0]), finiteField
                                .minus(Integer.parseInt(sharePerByte[j][0]), Integer.parseInt(sharePerByte[i][0])));
                        temp = finiteField.multiply(temp, fraction); // temp *= fraction
                    }
                }
                result = finiteField.add(result, temp); // result += temp
            }
            secret[k - 1] = (byte) ((char) (result));
        }
        return secret;
    }

    public byte[] getSecretEX() {
        if (shares.length < t) {
            throw new IllegalStateException("not enough share to recover the secret");
        }
        int result = 0;

        int numOfByte = shares[0].length();
        System.out.println(numOfByte);
        byte[] secret = new byte[numOfByte - 1];// 1st is not share

        for (int k = 1; k < numOfByte; k++) {
            result = 0;
            for (int i = 0; i < t; i++) {
                int temp = (((int) shares[i].charAt(k)) & 0xff); // temp = Y(i)
                for (int j = 0; j < t; j++) {
                    if (i != j) {
                        int fraction = finiteField.divide(((int) (shares[j].charAt(0))), finiteField
                                .minus(((int) (shares[j].charAt(0))), ((int) (shares[i].charAt(0)))));
                        temp = finiteField.multiply(temp, fraction); // temp *= fraction
                    }
                }
                result = finiteField.add(result, temp); // result += temp
            }
            secret[k - 1] = (byte) ((char) (result));
        }
        return secret;
    }

}
