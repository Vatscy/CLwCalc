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
}
