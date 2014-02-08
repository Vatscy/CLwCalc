import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/** An instance of this class calculate a formula of CLw. */
public class CLwCalc {
	public static void main(String[] args) {
		CLwCalc clw = new CLwCalc();

		String formula;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Start CLw Calculator.");
			System.out.println("(Write 'exit' to finish.)");
			System.out.print("Write a formula of CLw. : ");
			while (!(formula = br.readLine()).equals("exit")) {
				System.out.print(formula + " |> ");
				System.out.println(clw.normalize(formula));
				System.out.print("Write a formula of CLw. : ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** CLw式の正規化を行う。
	 * 
	 * @param formula
	 *            式
	 * @return 与えられた式の正規形。 */
	public String normalize(String formula) {
		formula = removeParentheses(formula);
		formula = replace(formula);

		String before = "";
		List<String> separated;
		StringBuilder newFormula = new StringBuilder();
		while (!formula.equals(before)) {
			before = formula;

			// K規則の適用
			formula = calcK(formula);
			// S規則の適用
			formula = calcS(formula);

			// 内部構造についてもK,S規則の適用
			separated = separateFormula(formula);
			newFormula.delete(0, newFormula.length());
			String block;
			for (int i = 0; i < separated.size(); i++) {
				block = removeParentheses(separated.get(i));
				block = calcK(block);
				block = calcS(block);
				if (i > 0) {
					block = addParentheses(block);
				}
				newFormula.append(block);
			}

			formula = newFormula.toString();
		}
		return formula;
	}

	String replace(String formula) {
		return formula.replaceAll("W", "(SS(KI))")
				.replaceAll("C", "(S(BBS)(KK))").replaceAll("B", "(S(KS)K)")
				.replaceAll("I", "(SKK)");
	}

	/** KMN |>1 M
	 * 
	 * @param formula
	 *            a formula
	 * @return if the formula is KMN, return M, else return the formula. */
	String calcK(String formula) {
		List<String> separated = separateFormula(formula);

		if (separated == null || separated.size() < 3
				|| !separated.get(0).equals("K")) {
			return formula;
		}

		String center = removeParentheses(separated.get(1));

		// 計算に関係のない残りの部分を再結合
		StringBuilder newFormulaBuilder = new StringBuilder(center);
		for (int i = 3; i < separated.size(); i++) {
			newFormulaBuilder.append(separated.get(i));
		}

		return newFormulaBuilder.toString();
	}

	/** SMNR |>1 MR(NR)
	 * 
	 * @param formula
	 *            a formula
	 * @return if the formula is SMNR, return MR(NR), else return the formula. */
	String calcS(String formula) {
		List<String> separated = separateFormula(formula);

		if (separated == null || separated.size() < 4
				|| !separated.get(0).equals("S")) {
			return formula;
		}

		String left = removeParentheses(separated.get(1));
		String center = removeParentheses(separated.get(2));
		String right = separated.get(3);

		// 計算に関係のない残りの部分を再結合
		StringBuilder newFormulaBuilder = new StringBuilder(left);
		newFormulaBuilder.append(right).append("(").append(center)
				.append(right).append(")");
		for (int i = 4; i < separated.size(); i++) {
			newFormulaBuilder.append(separated.get(i));
		}

		return newFormulaBuilder.toString();
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
	List<String> separate(String formula, List<String> separated) {
		if (formula.equals("")) {
			return separated;
		}

		int nextBeginIndex = 1;
		if (formula.startsWith("(")) {
			// "("から始まる場合は対応する")"を探し、そこまでが1ブロック
			StringBuilder builder = new StringBuilder(formula);
			int count = 1;
			for (int i = 1; i < builder.length(); i++) {
				if (builder.charAt(i) == '(') {
					count++;
				} else if (builder.charAt(i) == ')') {
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

	/** 式を括弧で括る。
	 * 
	 * @param formula
	 *            式
	 * @return 括弧で括った式 */
	String addParentheses(String formula) {
		if (formula != null && formula.length() > 1) {
			formula = "(" + formula + ")";
		}
		return formula;
	}

	/** 括弧で囲まれた式から括弧を取り除く。
	 * 
	 * @param formula
	 *            式
	 * @return 両サイドの括弧が取り除かれた式 */
	String removeParentheses(String formula) {
		if (formula == null || formula.equals("")) {
			return formula;
		}
		StringBuilder builder = new StringBuilder(formula);
		while (builder.charAt(0) == '(') {
			int count = 1;
			for (int i = 1; i < builder.length(); i++) {
				if (builder.charAt(i) == '(') {
					count++;
				} else if (builder.charAt(i) == ')') {
					count--;
					if (count <= 0) {
						builder.deleteCharAt(i).deleteCharAt(0);
						break;
					}
				}
			}
		}
		return builder.toString();
	}
}
