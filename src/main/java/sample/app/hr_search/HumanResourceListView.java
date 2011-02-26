package sample.app.hr_search;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sample.app.hr_management.HumanResourceView;
import sample.app.work_management.WorkListView;
import sample.common.console.Console;
import sample.common.console.View;
import sample.common.program.AbstractDispatcher;
import sample.domain.HumanResource;
import sample.domain.Occupation;
import sample.domain.Work;
import sample.repository.HumanResourceRepository;
import sample.repository.OccupationRepository;
import sample.repository.WorkRepository;

@Component
public class HumanResourceListView extends AbstractDispatcher implements View<List<HumanResource>> {

	/**
	 * 機能一覧
	 */
	private static final String[] MENU_LIST = { "P->前の10件\tN->次の10件", "E->検索一覧終了( 検索条件指定に戻る )" };

	/**
	 * メニュー文字列
	 */
	private static final List<String> CODE_LIST = Arrays.asList("人材ID", "P", "N", "E" );

	@Inject
	private HumanResourceRepository hrRepository;
	
	@Inject
	private WorkRepository workRespository;
	
	@Inject
	private OccupationRepository occupationRepository;

	@Inject
	private WorkListView workListView;
	
	@Inject
	private HumanResourceView hrView;

	@Autowired
	protected Console console;

	
	/**
	 * 業種リスト
	 */
	private List<Occupation> occupationList;
	
	private List<HumanResource> hrList;
	
	/**
	 * ページ番号
	 */
	private int page = 1;
	
	@PostConstruct
	public void init() {
		occupationList = occupationRepository.findAll();	
	}
	
	public void next() {
		page++;
	}

	public void previous() {
		page--;
	}

	@Override
	protected boolean isEndCommand(String inputCode) {
		return "E".equals(inputCode);
	}

	@Override
	protected void beforeDisplayMenu() {
		displayHRListOnPage();
	}

	@Override
	protected void runFunction(String inputCode) {
		if ("P".equals(inputCode)) {
			 // 前の10件
			if (page > 1) {
				previous();
			}
		} else if ("N".equals(inputCode)) {
			// 次の10件
			next();
		} else { // 人材ID入力
			displayHumanResource(inputCode);
		}		
	}
	
	/**
	 * 人材一覧の表示
	 * 
	 * @return 表示件数
	 */
	@Override
	public void display(List<HumanResource> hrList) {
		this.hrList = hrList;

		run();
	}

	@Override
	protected String printMenuAndWaitForInput() {
		console.display(""); // 改行
		console.display(MENU_LIST); // 機能一覧の表示
		
		return console.acceptFromList(CODE_LIST, "");
	}
	
	private void displayHRListOnPage() {
		while (true) { // 人材情報がヒットしなければ表示を繰り返す
			if (doDisplayHRListOnPage() > 0 || page == 1)
				break; // 人材一覧が表示されたか1ページ目のときにループを抜ける
			
			previous(); // 表示するレコードがなければ1ページ前を表示
		}
	}
	
	private int doDisplayHRListOnPage() {
		console.display("検索結果一覧");
		int count = 0; // 表示件数

		for (int i = (page - 1) * 10; i < page * 10; i++) {
			if (i >= hrList.size()) break;
			
			HumanResource hr = hrList.get(i);
			
			console.display(
				hr.getId() + "\t" + hr.getName() + "\t" 
				+ ((hr.getName().length() < 4) ? "\t" : "") // 3文字以下の名前のときタブ追加
				+ getOccupationName(hr.getOccupationId()));

			count++; // 表示件数をカウント
		}
		
		if (count == 0) {
			console.display("人材情報はありません。\n");
		}
		
		return count;
	}
	

	private void displayHumanResource(String inputCode) {
		try {
			HumanResource hr = hrRepository.findById(Long.parseLong(inputCode));
			hrView.display(hr);

			// 人材IDのセット
			if (hr == null) { // 人材情報を表示
				console.display("入力された人材情報は登録されていません。");
			}
			
			console.display("\n稼働状況---------------------------------------------");
			workListView.display(findWorkListByHRId(hr.getId()));
			
			console.accept("エンターキーを押すと検索結果一覧に戻ります。");
			
		} catch (NumberFormatException e) {
			console.display("入力された人材情報は登録されていません。");
		}
	}
	
	
	/**
	 * 業種IDから業種名の取得
	 * 
	 * @param occupationId
	 *            業種IDを表す文字列
	 * @return 業種名
	 */
	public String getOccupationName(long occupationId) {
		for (Occupation occupation : occupationList) {
			long id = occupation.getId();
			if (id == occupationId) {
				return occupation.getName();
			}
		}
		
		return null;
	}
	
	private List<Work> findWorkListByHRId(long hrId) {
		Work workExample = new Work();
		workExample.setHrId(hrId);
		return workRespository.findByExample(workExample);
	}

}
