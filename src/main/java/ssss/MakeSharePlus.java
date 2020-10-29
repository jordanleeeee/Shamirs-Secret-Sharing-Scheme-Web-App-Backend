package ssss;

import finiteField.FiniteField;


import java.util.Random;

// ssss over GF(2^n)
public class MakeSharePlus {

	final String secret;
	final int t;
	final int n;
	final int n_;
	final int limit;

	/**
	 * @param secret secret number
	 * @param t      threshold to recover the secret
	 * @param n      number of shares
	 * @param n_     value of n of GF(2^n)
	 */
	public MakeSharePlus(String secret, int t, int n, int n_) {
		if (t > n) {
			throw new IllegalStateException("n must be >= t in order to recover the secret");
		}
		this.secret = secret;
		this.t = t;
		this.n = n;
		this.n_ = n_;
		this.limit = (int) Math.pow(2, n_);
	}

	public String[] constructPoints() {
		Random random = new Random();
		int[] coefficient = new int[t];
		String[] shares = new String[n];
		byte[] secretByte = secret.getBytes();
		int [] temp=new int[secretByte.length];

		//byte to unsinged int, this is very important, don't remove
		for(int i=0;i<secretByte.length;i++)
		{
			temp[i]=secretByte[i]& 0xff;
//			System.out.println("temp[i]: "+temp[i]+"secretByte[i]: "+secretByte[i]);
		}
		for (int i = 0; i < n; i++) {
			shares[i] = Integer.toString(i + 1) + '-';
		}

		for (int k = 0; k < temp.length; k++) {

			int eachByte = temp[k];
			coefficient[0] = eachByte;
			for (int i = 1; i < coefficient.length; i++) {
				coefficient[i] = random.nextInt(limit);
			}
//			System.out.println("coefficient of polynomial: " + Arrays.toString(coefficient));

			for (int i = 0; i < shares.length; i++) {
				shares[i] += getYCoord(i + 1, coefficient);
				if (k != temp.length - 1)
					shares[i] += "-";
			}
		}
		return shares;
	}

	private int getYCoord(int x, int[] coefficient) {
		// y = f(x), input y, return x
		int val = 1;
		int result = 0;
		for (int coef : coefficient) {
			result = FiniteField.add(result, FiniteField.multiply(coef, val, n_)); // result += coef * x
			val = FiniteField.multiply(val, x, n_); // val *= x
		}
		return result;
	}

}
