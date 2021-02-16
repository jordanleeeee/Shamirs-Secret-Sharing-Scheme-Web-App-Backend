package finiteField;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ssss.RecoverSecretPlus;


public class FiniteFieldTest{
	static final FiniteField finiteField = FiniteField.getInstance();
	
	@Test
	public void addTest() {
		assertEquals(finiteField.add(0, 0), 0);
		assertEquals(finiteField.add(1, 0), 1);
		assertEquals(finiteField.add(0, 1), 1);
	}
	
	@Test
	public void minusTest() {
		assertEquals(finiteField.minus(0, 0), 0);
		assertEquals(finiteField.minus(1, 0), 1);
		assertEquals(finiteField.minus(0, 1), 1);
	}
	
	@Test
	public void multiplyTest() {
		assertEquals(finiteField.multiply(0, 0), 0);
	}
	
	@Test
	public void divisionTest() {
		assertEquals(finiteField.divide(0, 0), 0);
	}
	
	@Test
	public void combineTest() {
		assertEquals(finiteField.minus(finiteField.add(1, 2), 2), 1);
		assertEquals(finiteField.minus(finiteField.add(2, 3), 3), 2);
		assertEquals(finiteField.divide(finiteField.multiply(1, 2), 2), 1);
		assertEquals(finiteField.divide(finiteField.multiply(2, 3), 3), 2);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void invalidUsageTest1() {
		finiteField.multiply(256, 0);
	}
	@Test(expected = IndexOutOfBoundsException.class)
	public void invalidUsageTest2() {
		finiteField.divide(999, 1000);
	}
}