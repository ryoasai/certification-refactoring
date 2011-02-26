package sample.app.work_management;


import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.ValidInput;
import sample.common.program.Function;
import sample.domain.Work;
import sample.domain.WorkKey;
import sample.repository.HumanResourceRepository;
import sample.repository.WorkRepository;

/**
 * 稼働状況削除
 */
@Component
public class DeleteWorkFunction implements Function {

	@Inject
	private WorkRepository workRepository;

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private WorkListView workListView;
	
	@Inject
	private Console console;

	public void run() {

		// 人材ID入力
		long hrId = console.acceptLong("人材IDを入力してください。", new ValidInput<Long>() {
			@Override
			public boolean isValid(Long input) { // 人材ID存在チェック
				return hrRepository.findById(input) != null;
			}
		});

		// 人材IDに関連する稼動リストの検索
		List<Work> workList = findWorkListByHRId(hrId);
		if (workList.isEmpty()) {
			return;
		}
		
		 // 稼働状況を表示
		workListView.display(workList);
		
		// 削除する稼働状況IDの取得
		String workId = console.acceptFromIdList(workList, "削除したい稼働状況の番号を入力してください。");

		if (console.confirm("この情報を削除しますか？(Y はい　N いいえ)", "Y", "N")) {
			workRepository.delete(new WorkKey(hrId, Long.parseLong(workId)));
			console.display("削除しました。"); 
		}
	}

	private List<Work> findWorkListByHRId(long hrId) {
		Work workExample = new Work();
		workExample.setHrId(hrId);
		return workRepository.findByExample(workExample);
	}

}
