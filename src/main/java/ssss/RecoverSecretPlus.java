package ssss;




import finiteField.FiniteField;

public class RecoverSecretPlus {

	final String[] shares;
	final int t;
	final int n_;
	final int limit;

	/**
	 * @param shares share points
	 * @param t      threshold to recover the secret
	 * @param n_     value of n of GF(2^n)
	 */
	public RecoverSecretPlus(String[] shares, int t, int n_) {
		this.shares = shares;
		this.t = t;
		this.n_ = n_;
		this.limit = (int) Math.pow(2, n_);
	}

	// refer to formula
	// https://en.wikipedia.org/wiki/Shamir%27s_Secret_Sharing#Computationally_efficient_approach
	public byte[] getSecret() {
		if (shares.length < t) {
			throw new IllegalStateException("not enough share to recover the secret");
		}

		int result=0 ;

		int numOfByte = shares[0].split("-").length;
		byte[] secret = new byte[numOfByte - 1];// 1st is not share
		String[][] sharePerByte = new String[t][numOfByte];
		for (int i = 0; i < t; i++) {
			sharePerByte[i] = shares[i].split("-");
//			System.out.println( Arrays.deepToString(sharePerByte[i]));
		}

		for (int k = 1; k < numOfByte; k++) {
			result = 0;
			for (int i = 0; i < t; i++) {

				int temp = Integer.parseInt(sharePerByte[i][k]); // temp = Y(i)
				for (int j = 0; j < t; j++) {
					if (i != j) {

						int fraction = FiniteField.divide(Integer.parseInt(sharePerByte[j][0]), FiniteField
								.minus(Integer.parseInt(sharePerByte[j][0]), Integer.parseInt(sharePerByte[i][0])), n_);
						temp = FiniteField.multiply(temp, fraction, n_); // temp *= fraction
					}
				}
				result = FiniteField.add(result, temp); // result += temp

			}
			secret[k-1] = (byte) ((char) (result));
		
		}


		return secret;
	}

}