package ssss;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class MakeShareTest {
	
	private String[] shareParser(byte[][] shares) {
		String[] result = new String[shares.length];
		for(int i=0; i<shares.length; i++) {
			byte[] share = shares[i];
			char[] charTemp=new char [share.length];
			for(int j=0; j<share.length; j++) {
				charTemp[j] = (char)share[j];
			}
			result[i] = String.valueOf(charTemp);
		}
		return result;
	}
	
	private byte[] generateRandomSecret(int size) {
		Random random = new Random();
		int randSecretSize = random.nextInt(size);
		byte[] secret = new byte[randSecretSize];
		for(int i=0; i<randSecretSize; i++) {
			secret[i] = (byte)random.nextInt(256);
		}
		return secret;
	}
	
	@Test
	public void secretMatchWithRecoveredSecretTest1() {
		byte[] secret = generateRandomSecret(10);
		// generate random shares with t=3, n=5
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		byte[][] testShares = testSecret.constructPointsEX();
		assertEquals(testShares.length, 5);
		
		// try recover share from three random shares
		String[] shares = shareParser(testShares);
		String[] inputShare = new String[] {shares[0], shares[4], shares[3]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(inputShare, 3, 8);
		byte[] originalSecret = testRecover.getSecretEX();
		assertArrayEquals(secret, originalSecret);
	}
	
	@Test
	public void secretMatchWithRecoveredSecretTest2() {
		byte[] secret = generateRandomSecret(100);
		// generate random shares with t=3, n=5
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		byte[][] testShares = testSecret.constructPointsEX();
		assertEquals(testShares.length, 5);
		
		// try recover share from three random shares
		String[] shares = shareParser(testShares);
		String[] inputShare = new String[] {shares[0], shares[1], shares[2]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(inputShare, 3, 8);
		byte[] originalSecret = testRecover.getSecretEX();
		assertArrayEquals(secret, originalSecret);
	}
	
	@Test
	public void secretMatchWithRecoveredSecretTest3() {
		byte[] secret = generateRandomSecret(1000);
		// generate random shares with t=15, n=20
		MakeSharePlus testSecret = new MakeSharePlus(secret, 15, 20, 8);
		byte[][] testShares = testSecret.constructPointsEX();
		assertEquals(testShares.length, 20);
		
		// try recover share from all shares
		String[] shares = shareParser(testShares);
		RecoverSecretPlus testRecover = new RecoverSecretPlus(shares, 15, 8);
		byte[] originalSecret = testRecover.getSecretEX();
		assertArrayEquals(secret, originalSecret);
	}
	
	@Test
	public void secretMatchWithRecoveredSecretTest4() {
		byte[] secret = generateRandomSecret(10000);
		// generate random shares with t=2, n=3
		MakeSharePlus testSecret = new MakeSharePlus(secret, 2, 3, 8);
		byte[][] testShares = testSecret.constructPointsEX();
		assertEquals(testShares.length, 3);
		
		// try recover share from all shares
		String[] shares = shareParser(testShares);
		String[] inputShare = new String[] {shares[1], shares[0]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(inputShare, 2, 8);
		byte[] originalSecret = testRecover.getSecretEX();
		assertArrayEquals(secret, originalSecret);
	}
	
	@Test(expected = AssertionError.class)
	public void duplicatedShareRecoveryFailTest() {
		byte[] secret = generateRandomSecret(10);
		// generate random shares with t=3, n=5
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		byte[][] testShares = testSecret.constructPointsEX();
		
		// try recover share from two duplicate valid share and one valid share
		
		String[] shares = shareParser(testShares);
		shares[0] = shares[1];
		String[] inputShare = new String[] {shares[0], shares[1], shares[2]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(inputShare, 3, 8);
		byte[] originalSecret = testRecover.getSecretEX();
		assertArrayEquals(secret, originalSecret);
	}
	
	@Test(expected = AssertionError.class)
	public void insufficientShareRecoveryFailTest() {
		byte[] secret = generateRandomSecret(10);
		// generate random shares with t=3, n=5
		MakeSharePlus testSecret = new MakeSharePlus(secret, 4, 5, 8);
		byte[][] testShares = testSecret.constructPointsEX();
		
		// try recover share from in sufficient share
		String[] shares = shareParser(testShares);
		String[] inputShare = new String[] {shares[0], shares[1], shares[2]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(inputShare, 3, 8);
		byte[] originalSecret = testRecover.getSecretEX();
		assertArrayEquals(secret, originalSecret);
	}
	
	@Test(expected = IllegalStateException.class)
	public void invalidUsageTest1() {
		byte[] secret = generateRandomSecret(10);
		MakeSharePlus testSecret = new MakeSharePlus(secret, 5, 3, 8);
	}
	@Test(expected = IllegalStateException.class)
	public void invalidUsageTest2() {
		byte[] secret = generateRandomSecret(10);
		MakeSharePlus testSecret = new MakeSharePlus(secret, 0, 0, 8);
	}
	@Test(expected = IllegalStateException.class)
	public void invalidUsageTest3() {
		String[] randomShares = new String[]{"hello", "world", "!!"};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(randomShares, 0, 8);
	}
}
