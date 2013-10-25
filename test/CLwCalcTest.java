import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class CLwCalcTest {

	private CLwCalc	clw;

	@Before
	public void setUp() {
		clw = new CLwCalc();
	}

	@Test
	public void testCalcK0() {
		assertThat(clw.calcK(null), nullValue());
		assertThat(clw.calcK(""), is(""));
		assertThat(clw.calcK("x"), is("x"));
		assertThat(clw.calcK("Sxyz"), is("Sxyz"));
		assertThat(clw.calcK("kxy"), is("kxy"));
		assertThat(clw.calcK("x(Kyz)"), is("x(Kyz)"));
	}

	@Test
	public void testCalcK1() {
		assertThat(clw.calcK("K"), is("K"));
		assertThat(clw.calcK("Kx"), is("Kx"));
		assertThat(clw.calcK("K(xy)"), is("K(xy)"));
	}

	@Test
	public void testCalcK2() {
		assertThat(clw.calcK("Kxy"), is("x"));
		assertThat(clw.calcK("K(xyz)w"), is("xyz"));
		assertThat(clw.calcK("Kw(xyz)"), is("w"));
	}

	@Test
	public void testCalcK3() {
		assertThat(clw.calcK("Kxyzwv"), is("xzwv"));
		assertThat(clw.calcK("K(xyz)wv"), is("xyzv"));
		assertThat(clw.calcK("Kw(xyz)v"), is("wv"));
	}

	@Test
	public void testCalcK4() {
		assertThat(clw.calcK("K(xy)(zw)v"), is("xyv"));
		assertThat(clw.calcK("Kx(y(zw))v"), is("xv"));
		assertThat(clw.calcK("K(x(zw)y)v"), is("x(zw)y"));
		assertThat(clw.calcK("K(xy(zw))(vu)(st)"), is("xy(zw)(st)"));
	}

	@Test
	public void testCalcS0() {
		assertThat(clw.calcS(null), nullValue());
		assertThat(clw.calcS(""), is(""));
		assertThat(clw.calcS("x"), is("x"));
		assertThat(clw.calcS("Kxyz"), is("Kxyz"));
		assertThat(clw.calcS("sxyz"), is("sxyz"));
		assertThat(clw.calcS("x(Sxyz)"), is("x(Sxyz)"));
	}

	@Test
	public void testCalcS1() {
		assertThat(clw.calcS("S"), is("S"));
		assertThat(clw.calcS("Sx"), is("Sx"));
		assertThat(clw.calcS("S(xy)"), is("S(xy)"));
		assertThat(clw.calcS("S(xyz)"), is("S(xyz)"));
		assertThat(clw.calcS("Sxy"), is("Sxy"));
		assertThat(clw.calcS("S(xy)z"), is("S(xy)z"));
	}

	@Test
	public void testCalcS2() {
		assertThat(clw.calcS("Sxys"), is("xs(ys)"));
		assertThat(clw.calcS("S(xy)zw"), is("xyw(zw)"));
		assertThat(clw.calcS("Sx(yz)w"), is("xw(yzw)"));
	}

	@Test
	public void testCalcS3() {
		assertThat(clw.calcS("Sxyzvw"), is("xz(yz)vw"));
		assertThat(clw.calcS("S(xyz)wvu"), is("xyzv(wv)u"));
		assertThat(clw.calcS("Sw(xyz)vu"), is("wv(xyzv)u"));
	}

	@Test
	public void testCalcS4() {
		assertThat(clw.calcS("S(xy)(zw)vu"), is("xyv(zwv)u"));
		assertThat(clw.calcS("Sx(y(zw))vu"), is("xv(y(zw)v)u"));
		assertThat(clw.calcS("S(x(zw)y)vu"), is("x(zw)yu(vu)"));
		assertThat(clw.calcS("S(xy(zw))(vu)(st)r"), is("xy(zw)(st)(vu(st))r"));
	}

	@Test
	public void testAddParentheses0() {
		assertThat(clw.addParentheses(null), nullValue());
		assertThat(clw.addParentheses(""), is(""));
		assertThat(clw.addParentheses("x"), is("x"));
	}

	@Test
	public void testAddParentheses1() {
		assertThat(clw.addParentheses("xy"), is("(xy)"));
		assertThat(clw.addParentheses("xyz"), is("(xyz)"));
		assertThat(clw.addParentheses("x(yz)"), is("(x(yz))"));
	}

	@Test
	public void testRemoveParentheses0() {
		assertThat(clw.removeParentheses(null), nullValue());
		assertThat(clw.removeParentheses(""), is(""));
		assertThat(clw.removeParentheses("x"), is("x"));
		assertThat(clw.removeParentheses("x(yz)"), is("x(yz)"));
	}

	@Test
	public void testRemoveParentheses1() {
		assertThat(clw.removeParentheses("(xy)"), is("xy"));
		assertThat(clw.removeParentheses("(xyz)"), is("xyz"));
		assertThat(clw.removeParentheses("(x(yz))"), is("x(yz)"));
	}
}
