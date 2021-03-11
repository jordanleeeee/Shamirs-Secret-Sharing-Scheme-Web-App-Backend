package finiteField;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;


public class FiniteFieldTest{
	static final FiniteField finiteField = FiniteField.getInstance();
	
    /**
     * get result of finite field operation from online finite field calculator
     * http://www.ece.unb.ca/cgi-bin/tervo/calc2.pl
     * @param a first value
     * @param b second value
     * @param operator one of + - * / operator
     * @return answer
     */
	private int onlineFiniteFieldCalculator(int a, int b, char operator){
        int ans = -1;
        try {
            char f;
            switch (operator) {
                case '+': f = 'a'; break;
                case '-': f = 's'; break;
                case '*': f = 'm'; break;
                case '/': f = 'd'; break;
                default: throw new IllegalStateException();
            }
            String url = String.format("http://www.ee.unb.ca/cgi-bin/tervo/calc2.pl?num=%d&den=%d&f=%s&p=36", a, b, f);
            Connection web = Jsoup.connect(url);
            Document htmlDoc = Jsoup.parse(web.get().html());
            if (operator == '/') {
                Elements elements = htmlDoc.select("div > table > tbody > tr > td").select(".tdr");
                ans = Integer.parseInt(elements.first().html());
            } else {
                Elements elements = htmlDoc.select("div > table > tbody > tr > td");
                ans = Integer.parseInt(elements.last().html());
            }
            return ans;
        } catch(IOException e){
            return ans;
        }
    }
	
	@Test
	public void addTest() {
		for(int i=0; i<5; i++) {
			for(int j=251; j<256; j++) {
				assertEquals(finiteField.add(i, j), onlineFiniteFieldCalculator(i, j, '+'));
			}
		}
	}
	
	@Test
	public void minusTest() {
		for(int i=251; i<256; i++) {
			for(int j=0; j<5; j++) {
				assertEquals(finiteField.minus(i, j), onlineFiniteFieldCalculator(i, j, '-'));
			}
		}
	}
	
	@Test
	public void multiplyTest() {
		for(int i=50; i<60; i+=2) {
			for(int j=150; j<160; j+=2) {
				assertEquals(finiteField.multiply(i, j), onlineFiniteFieldCalculator(i, j, '*'));
			}
		}
	}
	
	@Test
	public void divisionTest() {
		for(int i=200; i<210; i+=2) {
			for(int j=10; j<20; j+=2) {
				assertEquals(finiteField.divide(i, j), onlineFiniteFieldCalculator(i, j, '/'));
			}
		}
	}
	
	@Test
	public void combineTest() {
		Random random = new Random();
		int randA = random.nextInt(256);
		int randB = random.nextInt(256);
		int randC = random.nextInt(256);
		assertEquals(finiteField.minus(finiteField.add(randA, randB), randB), randA);
		assertEquals(finiteField.minus(finiteField.add(randB, randC), randC), randB);
		assertEquals(finiteField.divide(finiteField.multiply(randA, randB), randB), randA);
		assertEquals(finiteField.divide(finiteField.multiply(randB, randC), randC), randB);
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