package sample.app.hr_management;


import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.View;
import sample.domain.HumanResource;
import sample.domain.Occupation;
import sample.repository.OccupationRepository;

/**
 * 人材情報詳細表示
 */
@Component
public class HumanResourceView implements View<HumanResource> {

	public static final String[] FIELDS = {
		"人材ID", "氏名", "郵便番号", "住所", "電話番号", "FAX番号", "e-mailアドレス",
		"生年月日", "性別", "業種", "経験年数", "最終学歴", "希望単価" };
	
	@Inject
	private OccupationRepository occupationRepository;

	@Inject
	private Console console;
	
	private Map<Long, Occupation> occupationMap;

	@PostConstruct
	public void init() {
		occupationMap = occupationRepository.findAllAsMap();
	}

	public void display(HumanResource hr) {
		
		String occupationName = getOccupationName(hr.getOccupationId());
		
		console.display(""); // 改行
		String[] hrArray = hr.toArray();
		
		// 人材情報の表示
		// TODO かなり醜いコード
		for (int i = 0; i < FIELDS.length; i++) {
			StringBuilder sb = new StringBuilder(FIELDS[i] + " : ");
			
			if (i == 8) { // 性別の表示
				if (hrArray[i].equals("M")) {
					sb.append("男");
				} else if (hrArray[i].equals("F")) {
					sb.append("女");
				}
			
			} else if (i == 9) {
				sb.append(occupationName); // 業種名の表示
			} else {
				sb.append(hrArray[i]);
			}
			
			if (i == 10) {
				sb.append("年"); // 経験年数の表示
			} else if (i == 12) {
				sb.append("円"); // 希望単価の表示
			}
			
			if (i == 2 || i == 3 || i == 5 || i == 6 || i == 8 || i == 10) {
				sb.append("\n");
			} else {
				sb.append("\t ");
			}
			
			console.display(sb.toString());
		}
	}
	
	/**
	 * 業種IDより業種名の取得
	 * 
	 * @param occupationId 業種IDを表す文字列
	 * @return 業種名
	 */
	private String getOccupationName(long occupationId) {
		try {
			Occupation occupation = occupationMap.get(occupationId);
		
			if (occupation == null) return null;
			
			return occupation.getName();
		} catch (NumberFormatException ex) {
			return null;
		}
	}

}
