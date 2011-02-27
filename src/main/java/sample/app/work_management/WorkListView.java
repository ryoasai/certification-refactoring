package sample.app.work_management;


import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.View;
import sample.domain.Partner;
import sample.domain.Work;
import sample.repository.PartnerRepository;

/**
 * 稼働実績詳細表示
 */
@Component
public class WorkListView implements View<List<Work>> {
	
	private static final int DISPLAY_LIMIT = 100;

	@Inject
	private PartnerRepository partnerRepository;
	
	@Inject
	private Console console;
	
	/**
	 * 取引先レコードを保存するMap
	 */
	private Map<Long, Partner> partnerMap;

	@PostConstruct
	public void init() {
		partnerMap = partnerRepository.findAllAsMap();
	}

	
	/**
	 * 指定された人材IDから稼働状況マスタを検索し，人材IDの一致するレコードを 最大100件まで抜き出す
	 * 
	 * @return 稼働状況
	 */
	public void display(List<Work> workList) {
		for (Work work : workList.subList(0, Math.min(workList.size(), DISPLAY_LIMIT))) {

			console.display(
					work.getWorkStatusNo() + "\t" + 
					work.getStartDate() + "～" + work.getEndDate() + "\t" + 
					getPartnerName(work.getPartnerId()));
		}
	}
	
	/**
	 * 取引先IDより取引先会社名の取得
	 * 
	 * @param partnerId
	 *            取引先IDを表す文字列
	 * @return 取引先会社名
	 */
	private String getPartnerName(long partnerId) {
		try {
			Partner partner = partnerMap.get(partnerId);
			
			if (partner == null) return null;
			
			return partner.getName();
			
		} catch (NumberFormatException ex) {
			return null;
		}
	}

}
