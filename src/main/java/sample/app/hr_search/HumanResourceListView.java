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
	 * �@�\�ꗗ
	 */
	private static final String[] MENU_LIST = { "P->�O��10��\tN->����10��", "E->�����ꗗ�I��( ���������w��ɖ߂� )" };

	/**
	 * ���j���[������
	 */
	private static final List<String> CODE_LIST = Arrays.asList("�l��ID", "P", "N", "E" );

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
	 * �Ǝ탊�X�g
	 */
	private List<Occupation> occupationList;
	
	private List<HumanResource> hrList;
	
	/**
	 * �y�[�W�ԍ�
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
			 // �O��10��
			if (page > 1) {
				previous();
			}
		} else if ("N".equals(inputCode)) {
			// ����10��
			next();
		} else { // �l��ID����
			displayHumanResource(inputCode);
		}		
	}
	
	/**
	 * �l�ވꗗ�̕\��
	 * 
	 * @return �\������
	 */
	@Override
	public void display(List<HumanResource> hrList) {
		this.hrList = hrList;

		run();
	}

	@Override
	protected String printMenuAndWaitForInput() {
		console.display(""); // ���s
		console.display(MENU_LIST); // �@�\�ꗗ�̕\��
		
		return console.acceptFromList(CODE_LIST, "");
	}
	
	private void displayHRListOnPage() {
		while (true) { // �l�ޏ�񂪃q�b�g���Ȃ���Ε\�����J��Ԃ�
			if (doDisplayHRListOnPage() > 0 || page == 1)
				break; // �l�ވꗗ���\�����ꂽ��1�y�[�W�ڂ̂Ƃ��Ƀ��[�v�𔲂���
			
			previous(); // �\�����郌�R�[�h���Ȃ����1�y�[�W�O��\��
		}
	}
	
	private int doDisplayHRListOnPage() {
		console.display("�������ʈꗗ");
		int count = 0; // �\������

		for (int i = (page - 1) * 10; i < page * 10; i++) {
			if (i >= hrList.size()) break;
			
			HumanResource hr = hrList.get(i);
			
			console.display(
				hr.getId() + "\t" + hr.getName() + "\t" 
				+ ((hr.getName().length() < 4) ? "\t" : "") // 3�����ȉ��̖��O�̂Ƃ��^�u�ǉ�
				+ getOccupationName(hr.getOccupationId()));

			count++; // �\���������J�E���g
		}
		
		if (count == 0) {
			console.display("�l�ޏ��͂���܂���B\n");
		}
		
		return count;
	}
	

	private void displayHumanResource(String inputCode) {
		try {
			HumanResource hr = hrRepository.findById(Long.parseLong(inputCode));
			hrView.display(hr);

			// �l��ID�̃Z�b�g
			if (hr == null) { // �l�ޏ���\��
				console.display("���͂��ꂽ�l�ޏ��͓o�^����Ă��܂���B");
			}
			
			console.display("\n�ғ���---------------------------------------------");
			workListView.display(findWorkListByHRId(hr.getId()));
			
			console.accept("�G���^�[�L�[�������ƌ������ʈꗗ�ɖ߂�܂��B");
			
		} catch (NumberFormatException e) {
			console.display("���͂��ꂽ�l�ޏ��͓o�^����Ă��܂���B");
		}
	}
	
	
	/**
	 * �Ǝ�ID����Ǝ햼�̎擾
	 * 
	 * @param occupationId
	 *            �Ǝ�ID��\��������
	 * @return �Ǝ햼
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
