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
 * 人材情報入力処理
 */
@Component
public class InputHRFunction implements Function {

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private OccupationRepository occupationRespository;
	
	@Inject
	private Console console;
	
	private HumanResource selectedHumanResource;

	/**
	 * 人材管理(追加)の実行
	 */
	public void run() {
		
		selectedHumanResource = new HumanResource();
		selectedHumanResource.fromArray(inputData());
		
		hrRepository.create(selectedHumanResource);

		console.display("人材ID：" + selectedHumanResource.getId()
				+ " で登録されました。");
	}

	/**
	 * 人材情報の入力
	 * 
	 * @param occupationList
	 *            業種リストを表す文字列配列
	 * @return 入力情報
	 */
	public String[] inputData() {
		String[] data = new String[HumanResourceView.FIELDS.length];
		
		for (int i = 1; i < HumanResourceView.FIELDS.length; i++) {
			if (HumanResourceView.FIELDS[i].equals("性別")) {
				data[i] = console.accept(HumanResourceView.FIELDS[i] + "を入力してください。", new ValidInput<String>() {
					@Override
					public boolean isValid(String input) {
						return "M".equals(input) || "F".equals(input);
					}
				});
			
			} else if (HumanResourceView.FIELDS[i].equals("業種")) {
				data[i] = console.acceptFromIdList(occupationRespository.findAll(), HumanResourceView.FIELDS[i] + "を入力してください。");
			} else {
				data[i] = console.accept(HumanResourceView.FIELDS[i] + "を入力してください。");
			}
		}
		
		return data;
	}
}
