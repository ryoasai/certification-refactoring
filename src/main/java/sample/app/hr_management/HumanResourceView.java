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
 * �l�ޏ��ڍו\��
 */
@Component
public class HumanResourceView implements View<HumanResource> {

	public static final String[] FIELDS = {
		"�l��ID", "����", "�X�֔ԍ�", "�Z��", "�d�b�ԍ�", "FAX�ԍ�", "e-mail�A�h���X",
		"���N����", "����", "�Ǝ�", "�o���N��", "�ŏI�w��", "��]�P��" };
	
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
		
		console.display(""); // ���s
		String[] hrArray = hr.toArray();
		
		// �l�ޏ��̕\��
		// TODO ���Ȃ�X���R�[�h
		for (int i = 0; i < FIELDS.length; i++) {
			StringBuilder sb = new StringBuilder(FIELDS[i] + " : ");
			
			if (i == 8) { // ���ʂ̕\��
				if (hrArray[i].equals("M")) {
					sb.append("�j");
				} else if (hrArray[i].equals("F")) {
					sb.append("��");
				}
			
			} else if (i == 9) {
				sb.append(occupationName); // �Ǝ햼�̕\��
			} else {
				sb.append(hrArray[i]);
			}
			
			if (i == 10) {
				sb.append("�N"); // �o���N���̕\��
			} else if (i == 12) {
				sb.append("�~"); // ��]�P���̕\��
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
	 * �Ǝ�ID���Ǝ햼�̎擾
	 * 
	 * @param occupationId �Ǝ�ID��\��������
	 * @return �Ǝ햼
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
