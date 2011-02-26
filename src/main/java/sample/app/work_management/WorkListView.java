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
 * �ғ����яڍו\��
 */
@Component
public class WorkListView implements View<List<Work>> {
	
	private static final int DISPLAY_LIMIT = 100;

	@Inject
	private PartnerRepository partnerRepository;
	
	@Inject
	private Console console;
	
	/**
	 * ����惌�R�[�h��ۑ�����Map
	 */
	private Map<Long, Partner> partnerMap;

	@PostConstruct
	public void init() {
		partnerMap = partnerRepository.findAllAsMap();
	}

	
	/**
	 * �w�肳�ꂽ�l��ID����ғ��󋵃}�X�^���������C�l��ID�̈�v���郌�R�[�h�� �ő�100���܂Ŕ����o��
	 * 
	 * @return �ғ���
	 */
	public void display(List<Work> workList) {
		for (Work work : workList.subList(0, Math.min(workList.size(), DISPLAY_LIMIT))) {

			console.display(
					work.getWorkStatusNo() + "\t" + 
					work.getStartDate() + "�`" + work.getEndDate() + "\t" + 
					getPartnerName(work.getPartnerId()));
		}
	}
	
	/**
	 * �����ID��������Ж��̎擾
	 * 
	 * @param partnerId
	 *            �����ID��\��������
	 * @return ������Ж�
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
