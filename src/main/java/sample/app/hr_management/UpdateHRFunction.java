package sample.app.hr_management;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.ValidInput;
import sample.common.program.Function;
import sample.domain.HumanResource;
import sample.repository.HumanResourceRepository;
import sample.repository.OccupationRepository;

/**
 * 人材情報更新
 */
@Component
public class UpdateHRFunction implements Function {

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private OccupationRepository occupationRespository;
	
	@Inject
	private HumanResourceView hrView;
	
	@Inject
	private Console console;
	
	private HumanResource selectedHumanResource;

	/**
	 * 人材管理(更新)メニューの実行
	 */
	public void run() {
		selectHumanResource();
		
		int itemNo = inputItemNo();
		if (itemNo < 1 || itemNo > 12) { // 項目番号入力エラー
			console.display("項目番号の入力が正しくありません。");
			console.display("更新できませんでした。");
			return;
		}

		inputData(itemNo);

		// 入力された値をファイルに保存
		hrRepository.update(selectedHumanResource);
	}
	
	private void selectHumanResource() {
		// 人材ID入力
		long hrId = console.acceptLong("人材IDを入力してください。", new ValidInput<Long>() {
			@Override
			public boolean isValid(Long input) { // 人材ID存在チェック
				return hrRepository.findById(input) != null;
			}
		});

		selectedHumanResource = hrRepository.findById(hrId);
		
		hrView.display(selectedHumanResource);
	}
	
	
	private int inputItemNo() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n更新したい項目を入力してください。\n");
		
		displayMenuItems(sb);

		return console.acceptInt(sb.toString());
	}

	private void displayMenuItems(StringBuilder buff) {
		for (int i = 1; i < HumanResourceView.FIELDS.length; i++) {
			buff.append(i + "." + HumanResourceView.FIELDS[i]);
			// TODO かなり醜いロジックだが、現状のロジックを保存しておく。
			// 本来はタブ位置を汎用的に自動調整するロジックを書くべき
			
			if (i == 1 || i == 8 || i == 9)
				buff.append("\t");
			if (i == 3 || i == 5 || i == 7 || i == 10 || i == 12)
				buff.append("\n");
			else if (i != 6)
				buff.append("\t");
		}
		
		buff.append("\n [1-12]>");
	}

	/**
	 * 人材情報の入力
	 * 
	 * @param occupationList
	 *            業種リストを表す文字列配列
	 * @return 入力情報
	 */
	public void inputData(int itemNo) {
		String[] data = selectedHumanResource.toArray();

		if (itemNo == 8) {
			data[itemNo] = console.accept("更新後の値を入力してください。(M：男性 F：女性)\n[M,F]", new ValidInput<String>() {
				@Override
				public boolean isValid(String input) {
					return "M".equals(input) || "F".equals(input);
				}
			});
		
		} else if (itemNo == 9) {
			data[itemNo] = console.acceptFromIdList(occupationRespository.findAll(), "更新後の値を入力してください。");
		} else {
			data[itemNo] = console.accept("更新後の値を入力してください。");
		}
	
		selectedHumanResource.fromArray(data);
	}

}
