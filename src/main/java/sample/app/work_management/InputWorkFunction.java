package sample.app.work_management;


import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.ValidInput;
import sample.common.program.Function;
import sample.domain.Work;
import sample.repository.HumanResourceRepository;
import sample.repository.PartnerRepository;
import sample.repository.WorkRepository;

/**
 * 稼働状況入力
 */
@Component
public class InputWorkFunction implements Function {

	@Inject
	private WorkRepository workRepository;

	@Inject
	private HumanResourceRepository hrRepository;
	
	@Inject
	private PartnerRepository partnerRepository;

	@Inject
	private Console console;
	
	/**
	 * 稼働状況管理(追加)の実行
	 */
	public void run() {
		Work work = inputData();

		doCreate(work);
	}


	/**
	 * 稼働状況の入力
	 */
	private Work inputData() {
		Work work = new Work();
		
		long hrId = console.acceptLong("人材IDを入力してください。", new ValidInput<Long>() {
			@Override
			public boolean isValid(Long input) { // 人材ID存在チェック
				return hrRepository.findById(input) != null;
			}
		});

		work.setHrId(hrId);
		work.setPartnerId(Long.valueOf(console.acceptFromNameIdList(partnerRepository.findAll(), "取引先を選択してください。")));
		work.setStartDate(console.accept("稼動開始日を入力してください。"));
		work.setEndDate(console.accept("稼動終了日を入力してください。"));
		work.setContractSalary(console.accept("契約単価を入力してください。"));
		
		return work;
	}

	/**
	 * 稼働状況のファイルへの登録
	 */
	private void doCreate(Work work) {
		workRepository.create(work);
		
		console.display("登録されました。");
	}

}
