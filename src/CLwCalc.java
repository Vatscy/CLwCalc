import java.util.ArrayList;
import java.util.List;

/** An instance of this class calculate a formula of CLw. */
public class CLwCalc {
	public static void main(String[] args) {
		CLwCalc clw = new CLwCalc();

		String formula = "Kxy(zx)";
		if (args.length > 0) {
			formula = args[0];
		}

		System.out.print(formula + " [>1 ");
		System.out.println(clw.calcK(formula));
	}

	/** KMN [>1 M
	 * 
	 * @param formula
	 *            a formula
	 * @return if the formula is KMN, return M, else return the formula. */
	String calcK(String formula) {
		List<String> separated = separateFormula(formula);
		if (separated == null || !separated.get(0).equals("K")
				|| separated.size() < 3) {
			return formula;
		}

		String center = separated.get(1);
		// 両サイドの無駄な括弧は取り除く
		if (center.startsWith("(") && center.endsWith(")")) {
			center = center.substring(1, center.length() - 1);
		}

		// 計算に関係のない残りの部分を再結合
		StringBuilder newFormulaBuilder = new StringBuilder(center);
		for (int i = 3; i < separated.size(); i++) {
			newFormulaBuilder.append(separated.get(i));
		}

		return newFormulaBuilder.toString();
	}

	/** SMNR [>1 MR(NR)
	 * 
	 * @param formula
	 *            a formula
	 * @return if the formula is SMNR, return MR(NR), else return the formula. */
	String calcS(String formula) {
		// TODO SMNR [>1 MR(NR)
		return null;
	}

	/** 式をブロックに分割する。
	 * 括弧で囲まれた部分は1ブロック。
	 * それ以外は1文字1ブロック。
	 * 
	 * @param formula
	 *            分割対象の式
	 * @return ブロック分割したリスト */
	List<String> separateFormula(String formula) {
		if (formula == null || formula.equals("")) {
			return null;
		}

		List<String> separated = new ArrayList<String>();
		return separate(formula, separated);
	}

	/** 式をブロックに分割する。
	 * 括弧で囲まれた部分は1ブロック。
	 * それ以外は1文字1ブロック。
	 * 
	 * 再帰呼び出し用メソッド。
	 * 
	 * @param formula
	 *            分割対象の式
	 * @param separated
	 *            ブロックを格納するリスト
	 * @return ブロック分割したリスト */
	private List<String> separate(String formula, List<String> separated) {
		if (formula.equals("")) {
			return separated;
		}

		int nextBeginIndex = 1;
		if (formula.startsWith("(")) {
			// "("から始まる場合は対応する")"を探し、そこまでが1ブロック
			int count = 1;
			String str;
			for (int i = 1; i < formula.length(); i++) {
				str = formula.substring(i);
				if (str.startsWith("(")) {
					count++;
				} else if (str.startsWith(")")) {
					count--;
					if (count <= 0) {
						separated.add(formula.substring(0, i + 1));
						nextBeginIndex = i + 1;
						break;
					}
				}
			}
		} else {
			// "("から始まらない場合は最初の1文字が1ブロック
			separated.add(formula.substring(0, 1));
		}

		String rest = formula.substring(nextBeginIndex);
		return separate(rest, separated);
	}
}
