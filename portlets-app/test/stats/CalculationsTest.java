package stats;

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculationsTest {

	@Test
	public void testCalcReturnFromPriceHalf() {
		assertEquals(0.5, Calculations.calcReturnFromPrice(201, 200), 0.0000001);
	}
	@Test
	public void testCalcReturnFromPriceSmall() {
		assertEquals(0.3205, Calculations.calcReturnFromPrice(200.8765, 200.2345), 0.1);
	}
	@Test
	public void testCalcReturnFromPriceNegative() {
		assertEquals(-0.2, Calculations.calcReturnFromPrice(4990, 5000), 0.0000001);
	}
	@Test
	public void testCalcReturnFromPriceNegativeSmall() {
		assertEquals(-0.001, Calculations.calcReturnFromPrice(99999, 100000), 0.0000001);
	}
}
