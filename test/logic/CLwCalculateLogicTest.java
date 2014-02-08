package logic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CLwCalculateLogicTest {

	private CLwCalculateLogic	logic;

	@Rule
	public ExpectedException	thrown	= ExpectedException.none();

	@Before
	public void setUp() {
		logic = CLwCalculateLogic.getInstance();
	}

	@Test
	public void testNormalize_異常系_nullを渡す() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("文字列を指定してください。");
		logic.normalize(null);
	}

	@Test
	public void testNormalize_正常系_定数を含まない項を渡す() {
		assertThat(logic.normalize(""), is(""));
		assertThat(logic.normalize("x"), is("x"));
		assertThat(logic.normalize("xy"), is("xy"));
		assertThat(logic.normalize("x(yz)"), is("x(yz)"));
		assertThat(logic.normalize("ab(cde(fg)h)ij"), is("ab(cde(fg)h)ij"));
	}

	@Test
	public void testNormalize_正常系_定数Kを含む項を渡す() {
		assertThat(logic.normalize("K"), is("K"));
		assertThat(logic.normalize("Kx"), is("Kx"));
		assertThat(logic.normalize("Kxy"), is("x"));
		assertThat(logic.normalize("Kxyz"), is("xz"));
		assertThat(logic.normalize("xKyz"), is("xKyz"));
		assertThat(logic.normalize("x(Kyz)"), is("xy"));
	}

	@Test
	public void testNormalize_正常系_定数Sを含む項を渡す() {
		assertThat(logic.normalize("S"), is("S"));
		assertThat(logic.normalize("Sx"), is("Sx"));
		assertThat(logic.normalize("Sxy"), is("Sxy"));
		assertThat(logic.normalize("Sxyz"), is("xz(yz)"));
		assertThat(logic.normalize("xSyzw"), is("xSyzw"));
		assertThat(logic.normalize("x(Syz)w"), is("x(Syz)w"));
		assertThat(logic.normalize("x(Syzw)"), is("x(yw(zw))"));
	}

	@Test
	public void testNormalize_正常系_定数KSを含む項を渡す() {
		assertThat(logic.normalize("KS"), is("KS"));
		assertThat(logic.normalize("SK"), is("SK"));
		assertThat(logic.normalize("KSx"), is("S"));
		assertThat(logic.normalize("KSxy"), is("Sy"));
		assertThat(logic.normalize("KSxyz"), is("Syz"));
		assertThat(logic.normalize("KxSyzw"), is("xyzw"));
		assertThat(logic.normalize("SKxyz"), is("yz"));
		assertThat(logic.normalize("Kx(Syzw)"), is("x"));
		assertThat(logic.normalize("K(Sxy)zw"), is("xw(yw)"));
	}

	@Test
	public void testNormalize_正常系_定数WCBIを含む項を渡す() {
		assertThat(logic.normalize("Wxy"), is("xyy"));
		assertThat(logic.normalize("Cxyz"), is("xzy"));
		assertThat(logic.normalize("Bxyz"), is("x(yz)"));
		assertThat(logic.normalize("Ix"), is("x"));
	}

	@Test
	public void testCalcK_異常系_nullを渡す() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("文字列を指定してください。");
		logic.calcK(null);
	}

	@Test
	public void testCalcK_正常系_Kで始まらない項を渡す() {
		assertThat(logic.calcK(""), is(""));
		assertThat(logic.calcK("x"), is("x"));
		assertThat(logic.calcK("Sxyz"), is("Sxyz"));
		assertThat(logic.calcK("kxy"), is("kxy"));
		assertThat(logic.calcK("x(Kyz)"), is("x(Kyz)"));
	}

	@Test
	public void testCalcK_正常系_Kで始まるが公理型Kを適用できない項を渡す() {
		assertThat(logic.calcK("K"), is("K"));
		assertThat(logic.calcK("Kx"), is("Kx"));
		assertThat(logic.calcK("K(xy)"), is("K(xy)"));
	}

	@Test
	public void testCalcK_正常系_Kで始まり公理型Kを適用できる項を渡す() {
		assertThat(logic.calcK("Kxy"), is("x"));
		assertThat(logic.calcK("K(xyz)w"), is("xyz"));
		assertThat(logic.calcK("Kw(xyz)"), is("w"));
	}

	@Test
	public void testCalcK_正常系_Kで始まり公理型Kを適用でき後ろに変数が続いている項を渡す() {
		assertThat(logic.calcK("Kxyzwv"), is("xzwv"));
		assertThat(logic.calcK("K(xyz)wv"), is("xyzv"));
		assertThat(logic.calcK("Kw(xyz)v"), is("wv"));
	}

	@Test
	public void testCalcK_正常系_Kで始まり公理形Kを適用でき括弧が2組以上ある項を渡す() {
		assertThat(logic.calcK("K(xy)(zw)v"), is("xyv"));
		assertThat(logic.calcK("Kx(y(zw))v"), is("xv"));
		assertThat(logic.calcK("K(x(zw)y)v"), is("x(zw)y"));
		assertThat(logic.calcK("K(xy(zw))(vu)(st)"), is("xy(zw)(st)"));
	}

	@Test
	public void testCalcS_異常系_nullを渡す() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("文字列を指定してください。");
		logic.calcS(null);
	}

	@Test
	public void testCalcS_正常系_Sで始まらない項を渡す() {
		assertThat(logic.calcS(""), is(""));
		assertThat(logic.calcS("x"), is("x"));
		assertThat(logic.calcS("Kxyz"), is("Kxyz"));
		assertThat(logic.calcS("sxyz"), is("sxyz"));
		assertThat(logic.calcS("x(Sxyz)"), is("x(Sxyz)"));
	}

	@Test
	public void testCalcS_正常系_Sで始まるが公理型Sを適用できない項を渡す() {
		assertThat(logic.calcS("S"), is("S"));
		assertThat(logic.calcS("Sx"), is("Sx"));
		assertThat(logic.calcS("S(xy)"), is("S(xy)"));
		assertThat(logic.calcS("S(xyz)"), is("S(xyz)"));
		assertThat(logic.calcS("Sxy"), is("Sxy"));
		assertThat(logic.calcS("S(xy)z"), is("S(xy)z"));
	}

	@Test
	public void testCalcS_正常系_Sで始まり公理型Sを適用できる項を渡す() {
		assertThat(logic.calcS("Sxys"), is("xs(ys)"));
		assertThat(logic.calcS("S(xy)zw"), is("xyw(zw)"));
		assertThat(logic.calcS("Sx(yz)w"), is("xw(yzw)"));
	}

	@Test
	public void testCalcS_正常系_Sで始まり公理型Sを適用でき後ろに変数が続いている項を渡す() {
		assertThat(logic.calcS("Sxyzvw"), is("xz(yz)vw"));
		assertThat(logic.calcS("S(xyz)wvu"), is("xyzv(wv)u"));
		assertThat(logic.calcS("Sw(xyz)vu"), is("wv(xyzv)u"));
	}

	@Test
	public void testCalcS_正常系_Sで始まり公理形Sを適用でき括弧が2組以上ある項を渡す() {
		assertThat(logic.calcS("S(xy)(zw)vu"), is("xyv(zwv)u"));
		assertThat(logic.calcS("Sx(y(zw))vu"), is("xv(y(zw)v)u"));
		assertThat(logic.calcS("S(x(zw)y)vu"), is("x(zw)yu(vu)"));
		assertThat(logic.calcS("S(xy(zw))(vu)(st)r"), is("xy(zw)(st)(vu(st))r"));
	}

	@Test
	public void testWrapTerm_異常系_nullを渡す() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("文字列を指定してください。");
		logic.wrapTerm(null);
	}

	@Test
	public void testWrapTerm_正常系_括弧で括る必要のない項を渡す() {
		assertThat(logic.wrapTerm(""), is(""));
		assertThat(logic.wrapTerm("x"), is("x"));
	}

	@Test
	public void testWrapTerm_正常系_長さ2以上の項を渡す() {
		assertThat(logic.wrapTerm("xy"), is("(xy)"));
		assertThat(logic.wrapTerm("xyz"), is("(xyz)"));
		assertThat(logic.wrapTerm("x(yz)"), is("(x(yz))"));
	}

	@Test
	public void testUnwrapTerm_異常系_nullを渡す() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("文字列を指定してください。");
		logic.unwrapTerm(null);
	}

	@Test
	public void testUnwrapTerm_正常系_括弧で括られていない項を渡す() {
		assertThat(logic.unwrapTerm(""), is(""));
		assertThat(logic.unwrapTerm("x"), is("x"));
		assertThat(logic.unwrapTerm("x(yz)"), is("x(yz)"));
	}

	@Test
	public void testUnwrapTerm_正常系_括弧で括られた項を渡す() {
		assertThat(logic.unwrapTerm("(xy)"), is("xy"));
		assertThat(logic.unwrapTerm("(xyz)"), is("xyz"));
		assertThat(logic.unwrapTerm("(x(yz))"), is("x(yz)"));
	}
}
