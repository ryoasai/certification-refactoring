package sample.common.program;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import sample.common.console.Console;

/**
 * 人材派遣管理プログラムメインクラス
 */
public abstract class AbstractMainProgram extends AbstractDispatcher implements MainProgram {

	@Resource
	protected Map<String, Function> functionMap = new HashMap<String, Function>();

	@Autowired
	protected Console console;

	public Map<String, Function> getFunctionMap() {
		return functionMap;
	}

	public void setFunctionMap(Map<String, Function> functionMap) {
		this.functionMap = functionMap;
	}
	
	protected boolean isConfirm(String inputCode) {
		return !"S".equals(inputCode);
	}

	/**
	 * 機能一覧と機能コード一覧を表示し，機能コードを取得して該当の機能を呼び出す
	 */
	@Override
	protected void runFunction(String inputCode) {
		Function subFunction = functionMap.get(inputCode);
		if (subFunction == null) return;
		
		assert subFunction != null;
		
		try {
			subFunction.run();
		} catch (Exception ex) {
			// TODO 適切な例外処理
			ex.printStackTrace();
		}
		
		if (isConfirm(inputCode)) { // 人材管理と稼働状況管理のみ
			console.accept("エンターキーを押すとメニューに戻ります。");
		}
	}
}
