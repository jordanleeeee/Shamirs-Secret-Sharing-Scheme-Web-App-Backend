package ssss;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class MakeShareTest {
	
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
	public void secretMatchRecoveredSecretTest1() {
		byte[] secret = generateRandomSecret(10);
		// generate random shares with t=3, n=5
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		String[] testShares = testSecret.constructPoints();
		assertEquals(testShares.length, 5);
		
		// try recover share from three random shares
		String[] shares = new String[] {testShares[0], testShares[4], testShares[3]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(shares, 3, 8);
		byte[] originalSecret = testRecover.getSecret();
		assertArrayEquals(secret, originalSecret);
	}
	@Test
	public void secretMatchRecoveredSecretTest2() {
		byte[] secret = generateRandomSecret(100);
		// generate random shares with t=3, n=5
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		String[] testShares = testSecret.constructPoints();
		assertEquals(testShares.length, 5);
		
		// try recover share from three random shares
		String[] shares = new String[] {testShares[0], testShares[1], testShares[2]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(shares, 3, 8);
		byte[] originalSecret = testRecover.getSecret();
		assertArrayEquals(secret, originalSecret);
	}
	@Test
	public void secretMatchRecoveredSecretTest3() {
		byte[] secret = generateRandomSecret(1000);
		// generate random shares with t=15, n=20
		MakeSharePlus testSecret = new MakeSharePlus(secret, 15, 20, 8);
		String[] testShares = testSecret.constructPoints();
		assertEquals(testShares.length, 20);
		
		// try recover share from all shares
		RecoverSecretPlus testRecover = new RecoverSecretPlus(testShares, 15, 8);
		byte[] originalSecret = testRecover.getSecret();
		assertArrayEquals(secret, originalSecret);
	}
	@Test
	public void secretMatchRecoveredSecretTest4() {
		byte[] secret = generateRandomSecret(10000);
		// generate random shares with t=2, n=3
		MakeSharePlus testSecret = new MakeSharePlus(secret, 2, 3, 8);
		String[] testShares = testSecret.constructPoints();
		assertEquals(testShares.length, 3);
		
		String[] shares = new String[] {testShares[1], testShares[0]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(shares, 2, 8);
		byte[] originalSecret = testRecover.getSecret();
		assertArrayEquals(secret, originalSecret);
	}
	
	@Test(expected = AssertionError.class)
	public void secretSecretFailTest1() {
		byte[] secret = generateRandomSecret(10);
		// generate random shares with t=3, n=5
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		String[] testShares = testSecret.constructPoints();
		
		// try recover share from two duplicate valid share and one valid share
		testShares[0] = testShares[1];
		String[] shares = new String[] {testShares[0], testShares[1], testShares[2]};
		RecoverSecretPlus testRecover = new RecoverSecretPlus(shares, 3, 8);
		byte[] originalSecret = testRecover.getSecret();
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
