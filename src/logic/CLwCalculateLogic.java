package logic;

import java.util.ArrayList;
import java.util.List;

/** 組み合わせ論理の体系CLwにおける各種計算を行う。 */
public class CLwCalculateLogic {

	private static CLwCalculateLogic	instance	= new CLwCalculateLogic();

	private CLwCalculateLogic() {
	}

	public static CLwCalculateLogic getInstance() {
		return instance;
	}

	/** CL-項の正規化を行う。
	 * 
	 * @param term
	 *            項
	 * @return 与えられたCL-項の正規形。 */
	public String normalize(String term) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}

		term = unwrapTerm(term);
		term = representTermWithKAndS(term);

		String before = "";
		List<String> separated;
		StringBuilder newterm = new StringBuilder();
		while (!term.equals(before)) {
			before = term;

			// K規則の適用
			term = calcK(term);
			// S規則の適用
			term = calcS(term);

			// 内部構造についてもK,S規則の適用
			separated = separateTerm(term);
			newterm.delete(0, newterm.length());
			String block;
			for (int i = 0; i < separated.size(); i++) {
				block = unwrapTerm(separated.get(i));
				block = calcK(block);
				block = calcS(block);
				if (i > 0) {
					block = wrapTerm(block);
				}
				newterm.append(block);
			}

			term = newterm.toString();
		}
		return term;
	}

	/** 定数W、C、B、IをS、Kを用いて表現する。
	 * 
	 * @param term
	 *            項
	 * @return W、C、B、IをS、Kで表現し直した項 */
	String representTermWithKAndS(String term) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}
		return term.replaceAll("W", "(SS(KI))").replaceAll("C", "(S(BBS)(KK))")
				.replaceAll("B", "(S(KS)K)").replaceAll("I", "(SKK)");
	}

	/** KMN |>1 M
	 * 
	 * @param term
	 *            項
	 * @return 公理型Kによりweak変形した結果の項。weak変形できない場合はそのまま返す。 */
	String calcK(String term) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}

		List<String> separated = separateTerm(term);

		if (separated.size() < 3 || !separated.get(0).equals("K")) {
			return term;
		}

		String center = unwrapTerm(separated.get(1));

		// 計算に関係のない残りの部分を再結合
		StringBuilder newtermBuilder = new StringBuilder(center);
		for (int i = 3; i < separated.size(); i++) {
			newtermBuilder.append(separated.get(i));
		}

		return newtermBuilder.toString();
	}

	/** SMNR |>1 MR(NR)
	 * 
	 * @param term
	 *            項
	 * @return 公理型Sによりweak変形した結果の項。weak変形できない場合はそのまま返す。 */
	String calcS(String term) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}

		List<String> separated = separateTerm(term);

		if (separated.size() < 4 || !separated.get(0).equals("S")) {
			return term;
		}

		String left = unwrapTerm(separated.get(1));
		String center = unwrapTerm(separated.get(2));
		String right = separated.get(3);

		// 計算に関係のない残りの部分を再結合
		StringBuilder newtermBuilder = new StringBuilder(left);
		newtermBuilder.append(right).append("(").append(center).append(right)
				.append(")");
		for (int i = 4; i < separated.size(); i++) {
			newtermBuilder.append(separated.get(i));
		}

		return newtermBuilder.toString();
	}

	/** 項をブロックに分割する。
	 * 括弧で囲まれた部分は1ブロック。
	 * それ以外は1文字1ブロック。
	 * 
	 * @param term
	 *            分割対象の項
	 * @return ブロック分割したリスト */
	List<String> separateTerm(String term) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}

		List<String> separated = new ArrayList<String>();
		return separate(term, separated);
	}

	/** 項をブロックに分割する。
	 * 括弧で囲まれた部分は1ブロック。
	 * それ以外は1文字1ブロック。
	 * 
	 * 再帰呼び出し用メソッド。
	 * 
	 * @param term
	 *            分割対象の項
	 * @param separated
	 *            ブロックを格納するリスト
	 * @return ブロック分割したリスト */
	List<String> separate(String term, List<String> separated) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}

		if (term.equals("")) {
			return separated;
		}

		int nextBeginIndex = 1;
		if (term.startsWith("(")) {
			// "("から始まる場合は対応する")"を探し、そこまでが1ブロック
			StringBuilder builder = new StringBuilder(term);
			int count = 1;
			for (int i = 1; i < builder.length(); i++) {
				if (builder.charAt(i) == '(') {
					count++;
				} else if (builder.charAt(i) == ')') {
					count--;
					if (count <= 0) {
						separated.add(term.substring(0, i + 1));
						nextBeginIndex = i + 1;
						break;
					}
				}
			}
		} else {
			// "("から始まらない場合は最初の1文字が1ブロック
			separated.add(term.substring(0, 1));
		}

		String rest = term.substring(nextBeginIndex);
		return separate(rest, separated);
	}

	/** 項を括弧で括る。
	 * 
	 * @param term
	 *            項
	 * @return 括弧で括った項 */
	String wrapTerm(String term) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}

		if (term.length() > 1) {
			term = "(" + term + ")";
		}
		return term;
	}

	/** 括弧で囲まれた項から括弧を取り除く。
	 * 
	 * @param term
	 *            項
	 * @return 両サイドの括弧が取り除かれた項 */
	String unwrapTerm(String term) {
		if (term == null) {
			throw new IllegalArgumentException("文字列を指定してください。");
		}

		if (term.equals("")) {
			return term;
		}
		StringBuilder builder = new StringBuilder(term);
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
