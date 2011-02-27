package sample.app.hr_search;


import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.program.AbstractDispatcher;
import sample.common.program.Function;
import sample.domain.HumanResource;
import sample.domain.Occupation;
import sample.repository.HumanResourceRepository;
import sample.repository.OccupationRepository;


/**
 * 人材検索処理
 */
@Component
public class SearchHRListFunction extends AbstractDispatcher implements Function {
	
	/**
	 * 検索方法一覧のリスト
	 */
	private static final String[] MENU_LIST = { 
	  "検索方法を指定してください。", 
	  "N->氏名から検索\tT->業種から検索",
	  "E->人材検索終了(メニューに戻る)" };

	/**
	 * 検索方法コード一覧のリスト
	 */
	private static final List<String> CODE_LIST =  Arrays.asList("N", "T", "E" );

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private OccupationRepository occupationRepository;

	@Inject
	private HumanResourceListView hrListView;

	@Inject
	private Console console;
	
	@Override
	protected String printMenuAndWaitForInput() {
		console.display("");
		console.display(MENU_LIST);
		
		return console.acceptFromList(CODE_LIST, "どの機能を実行しますか？");
	}
	
	/**
	 * 氏名もしくは業種から検索機能を呼び出す
	 * 
	 * @param code 機能コードを表す整数値
	 */
	@Override
	protected void runFunction(String code) {
		
		if ("N".equals(code)) {
			String input = console.accept("氏名に含まれる文字列を指定してください。");
			searchHRListByName(input);
			
		} else if ("T".equals(code)) {

			List<Occupation> occupationList = occupationRepository.findAll(); // 業種リストの取得
			String occupationType = console.acceptFromNameIdList(occupationList, "\n業種を選択してください。");
				
			// 業種IDから人材検索
			searchHRListByOccupationType(Long.valueOf(occupationType));
		}
	}

	private void searchHRListByName(String name) {
		
		HumanResource exampleHR = new HumanResource(); 
		exampleHR.setName(name);
		
		doSearchHRList(exampleHR);
	}

	private void searchHRListByOccupationType(long occupationId) {
		
		HumanResource exampleHR = new HumanResource(); 
		exampleHR.setOccupationId(occupationId);
		
		doSearchHRList(exampleHR);
	}
	
	private void doSearchHRList(HumanResource exampleHR) {
		List<HumanResource> hrList = hrRepository.findByExample(exampleHR);
		hrListView.display(hrList);
	}
}
