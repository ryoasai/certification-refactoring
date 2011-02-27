package sample;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import sample.common.program.AbstractMainProgram;
import sample.common.program.MainProgram;

/**
 * メインプログラム
 */
@Component("mainProgram")
public class TempHRManagementProgram extends AbstractMainProgram {

	/**
	 * 機能一覧
	 */
	private static final String[] MENU_LIST = {
			"_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/",
			"            人材管理システム",
			"                メニュー",
			"  [1].人材検索(S)",
			"  [2].人材管理(JI：追加 JU：更新 JD：削除)",
			"  [3].稼働状況管理(KI：追加 KD：削除)",
			"  [4].終了(E)",
			"_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/"};

	/**
	 * 機能コード一覧
	 */
	private static final List<String> CODE_LIST = Arrays.asList( "S", "JI", "JU", "JD", "KI", "KD", "E" );

	@Override
	protected String printMenuAndWaitForInput() {
		console.display(""); //改行
		console.display(MENU_LIST);
		return console.acceptFromList(CODE_LIST, "どの機能を実行しますか？");
	}
	
	/**
	 * メインエントリーポイント
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/app-context.xml");
		
		MainProgram mainProgram = context.getBean("mainProgram", MainProgram.class);
		mainProgram.run();
	}

}
