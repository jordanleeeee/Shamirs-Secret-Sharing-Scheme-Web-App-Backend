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
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		String[] testShares = testSecret.constructPoints();
		RecoverSecretPlus testRecover = new RecoverSecretPlus(testShares, 3, 8);
		byte[] originalSecret = testRecover.getSecret();
		assertArrayEquals(secret, originalSecret);
	}
	@Test
	public void secretMatchRecoveredSecretTest2() {
		byte[] secret = generateRandomSecret(100);
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		String[] testShares = testSecret.constructPoints();
		RecoverSecretPlus testRecover = new RecoverSecretPlus(testShares, 3, 8);
		byte[] originalSecret = testRecover.getSecret();
		assertArrayEquals(secret, originalSecret);
	}
	@Test
	public void secretMatchRecoveredSecretTest3() {
		byte[] secret = generateRandomSecret(1000);
		MakeSharePlus testSecret = new MakeSharePlus(secret, 15, 20, 8);
		String[] testShares = testSecret.constructPoints();
		RecoverSecretPlus testRecover = new RecoverSecretPlus(testShares, 15, 8);
		byte[] originalSecret = testRecover.getSecret();
		assertArrayEquals(secret, originalSecret);
	}
	@Test
	public void secretMatchRecoveredSecretTest4() {
		byte[] secret = generateRandomSecret(10000);
		MakeSharePlus testSecret = new MakeSharePlus(secret, 3, 5, 8);
		String[] testShares = testSecret.constructPoints();
		RecoverSecretPlus testRecover = new RecoverSecretPlus(testShares, 3, 8);
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
