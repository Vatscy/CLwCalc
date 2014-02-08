import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import logic.CLwCalculateLogic;

/** 組み合わせ論理の体系CLwにおけるCL-項の正規化を行う。 */
public class CLwCalculator {
	/** メイン処理。 */
	public static void main(String[] args) {
		CLwCalculateLogic logic = CLwCalculateLogic.getInstance();

		String term;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println(":::::CLw Calculator:::::");
			System.out.println("'!q'で終了します。");
			System.out.println();

			System.out.print("CL-項を入力 : ");
			while (!(term = br.readLine()).equals("!q")) {
				System.out.print(term + " |> ");
				System.out.println(logic.normalize(term));
				System.out.print("CL-項を入力 : ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
