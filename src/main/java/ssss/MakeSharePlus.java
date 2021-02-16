package ssss;

import finiteField.FiniteField;


import java.util.Random;

// ssss over GF(2^n)
public class MakeSharePlus {
	
	static final FiniteField finiteField = FiniteField.getInstance();
	final byte[] secret;
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
	public MakeSharePlus(byte[] secret, int t, int n, int n_) {
		if (t > n || n < 1) {
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
		int [] temp=new int[secret.length];

		//byte to unsinged int, this is very important, don't remove
		for(int i=0;i<secret.length;i++)
		{
			temp[i]=secret[i]& 0xff;
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
	
	public byte[][] constructPointsEX() {
		Random random = new Random();
		int[] coefficient = new int[t];
		byte[][] shares = new byte[n][secret.length+1];//1 for the x
		int [] temp=new int[secret.length];


		//byte to unsinged int, this is very important, don't remove
		for(int i=0;i<secret.length;i++)
		{
			temp[i]=secret[i]& 0xff;
//			System.out.println("temp[i]: "+temp[i]+"secretByte[i]: "+secretByte[i]);
		}
		for (int i = 0; i < n; i++) {
			shares[i][0] = (byte)(i + 1) ;
		}

		for (int k = 0; k < secret.length; k++) {

			int eachByte = secret[k]& 0xff;
			coefficient[0] = eachByte;
			for (int i = 1; i < coefficient.length; i++) {
				coefficient[i] = random.nextInt(limit);
			}
//			System.out.println("coefficient of polynomial: " + Arrays.toString(coefficient));

			for (int i = 0; i < shares.length; i++) {
				shares[i][k+1] = (byte)getYCoord(i + 1, coefficient);

			}
		}
		return shares;
	}

	private int getYCoord(int x, int[] coefficient) {
		// y = f(x), input y, return x
		int val = 1;
		int result = 0;
		for (int coef : coefficient) {
			result = finiteField.add(result, finiteField.multiply(coef, val)); // result += coef * x
			val = finiteField.multiply(val, x); // val *= x
		}
		return result;
	}

}
