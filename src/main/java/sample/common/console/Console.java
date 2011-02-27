package sample.common.console;

import java.util.Date;
import java.util.List;

import sample.common.entity.Identifiable;
import sample.common.entity.NameId;

/**
 * コンソール出力処理をカプセル化するためのインターフェース
 */
public interface Console {

	/**
	 * メッセージを表示する。
	 * @param messages 表示対象メッセージ
	 */
	void display(String... messages);

	/**
	 * YesNoの選択メッセージを表示する。
	 * @param message 表示対象メッセージ
	 * @return Yesが選択された場合はtrue
	 */
	boolean confirm(String message, String yes, String no);
	
	/**
	 * メッセージとともに入力プロンプトを表示し、標準入力からの入力を受け付ける
	 * 
	 * @param 表示メッセージ
	 * @return 入力文字列
	 */
	String accept(String message);

	/**
	 * メッセージとともに入力プロンプトを表示し、標準入力からの入力を受け付ける。
	 * 正しい入力値が得られるまで、再度入力を繰り返す。
	 * 
	 * @param 表示メッセージ
	 * @return 入力文字列
	 */
	String accept(String message, ValidInput<String> validInput);

	/**
	 * メッセージとともに入力プロンプトを表示し、標準入力からの整数入力を受け付ける
	 * 
	 * @param 表示メッセージ
	 * @return 入力値
	 */
	int acceptInt(String message);

	int acceptInt(String message, ValidInput<Integer> validInput);

	/**
	 * メッセージとともに入力プロンプトを表示し、標準入力からの長整数入力を受け付ける
	 * 
	 * @param 表示メッセージ
	 * @return 入力値
	 */
	long acceptLong(String message);

	long acceptLong(String message, ValidInput<Long> validInput);
	
	/**
	 * yyyyMMdd書式で日付を入力する。正しい日付が入力されるまで処理を繰り返す。
	 * 
	 * @param message 表示メッセージ
	 * @return 入力された日付
	 */
	Date acceptDate(String message);

	/**
	 * 日付を入力する。正しい日付が入力されるまで処理を繰り返す。
	 * 
	 * @param message 表示メッセージ
	 * @param format 日付フォーマット
	 * @return 入力された日付
	 */
	Date acceptDate(String message, String format);

	/**
	 * メッセージとともに選択肢のリストを表示する。
	 * 選択結果を返す。正しい選択結果が入力されるまで、内部で再入力を促す。
	 * 
	 * @param selectList
	 * @param message
	 * @return 選択結果
	 */
	String acceptFromNameIdList(List<? extends NameId<?>> selectList, String message);

	String acceptFromIdList(List<? extends Identifiable<?>> selectList, String message);

	String acceptFromList(List<String> selectList, String message);
}
