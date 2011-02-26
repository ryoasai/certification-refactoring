package sample.app.hr_management;


import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.ValidInput;
import sample.common.program.Function;
import sample.domain.HumanResource;
import sample.repository.HumanResourceRepository;

/**
 * �l�ޏ��폜
 */
@Component
public class DeleteHRFunction implements Function {

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private Console console;

	@Inject
	private HumanResourceView hrView;
	
	private HumanResource selectedHumanResource;

	/**
	 * �l�ފǗ�(�폜)���j���[�̎��s
	 */
	public void run() {
		selectHumanResource();
		
		deleteHumanResource();
	}

	private void selectHumanResource() {
		// �l��ID����
		long hrId = console.acceptLong("�l��ID����͂��Ă��������B", new ValidInput<Long>() {
			@Override
			public boolean isValid(Long input) { // �l��ID���݃`�F�b�N
				return hrRepository.findById(input) != null;
			}
		});
		
		selectedHumanResource = hrRepository.findById(hrId);
		
		hrView.display(selectedHumanResource);
	}
	
	private void deleteHumanResource() {
		if (console.confirm("���̐l�ޏ����폜���܂����H(Y �͂��@N ������)", "Y", "N")) {
			hrRepository.delete(selectedHumanResource.getId());
			console.display("�폜���܂����B"); 
		}
	}

}
