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
 * �l�ތ�������
 */
@Component
public class SearchHRListFunction extends AbstractDispatcher implements Function {
	
	/**
	 * �������@�ꗗ�̃��X�g
	 */
	private static final String[] MENU_LIST = { 
	  "�������@���w�肵�Ă��������B", 
	  "N->�������猟��\tT->�Ǝ킩�猟��",
	  "E->�l�ތ����I��(���j���[�ɖ߂�)" };

	/**
	 * �������@�R�[�h�ꗗ�̃��X�g
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
		
		return console.acceptFromList(CODE_LIST, "�ǂ̋@�\�����s���܂����H");
	}
	
	/**
	 * �����������͋Ǝ킩�猟���@�\���Ăяo��
	 * 
	 * @param code �@�\�R�[�h��\�������l
	 */
	@Override
	protected void runFunction(String code) {
		
		if ("N".equals(code)) {
			String input = console.accept("�����Ɋ܂܂�镶������w�肵�Ă��������B");
			searchHRListByName(input);
			
		} else if ("T".equals(code)) {

			List<Occupation> occupationList = occupationRepository.findAll(); // �Ǝ탊�X�g�̎擾
			String occupationType = console.acceptFromNameIdList(occupationList, "\n�Ǝ��I�����Ă��������B");
				
			// �Ǝ�ID����l�ތ���
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
