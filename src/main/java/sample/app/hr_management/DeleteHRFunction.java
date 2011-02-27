package sample.app.hr_management;


import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.ValidInput;
import sample.common.program.Function;
import sample.domain.HumanResource;
import sample.repository.HumanResourceRepository;

/**
 * 人材情報削除
 */
@Component
public class DeleteHRFunction implements Function {

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private Console console;

	@Inject
	private HumanResourceView hrView;
	
	private HumanResource selectedHumanResource;

	/**
	 * 人材管理(削除)メニューの実行
	 */
	public void run() {
		selectHumanResource();
		
		deleteHumanResource();
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
	
	private void deleteHumanResource() {
		if (console.confirm("この人材情報を削除しますか？(Y はい　N いいえ)", "Y", "N")) {
			hrRepository.delete(selectedHumanResource.getId());
			console.display("削除しました。"); 
		}
	}

}
