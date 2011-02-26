package sample.app.work_management;


import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.ValidInput;
import sample.common.program.Function;
import sample.domain.Work;
import sample.domain.WorkKey;
import sample.repository.HumanResourceRepository;
import sample.repository.WorkRepository;

/**
 * �ғ��󋵍폜
 */
@Component
public class DeleteWorkFunction implements Function {

	@Inject
	private WorkRepository workRepository;

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private WorkListView workListView;
	
	@Inject
	private Console console;

	public void run() {

		// �l��ID����
		long hrId = console.acceptLong("�l��ID����͂��Ă��������B", new ValidInput<Long>() {
			@Override
			public boolean isValid(Long input) { // �l��ID���݃`�F�b�N
				return hrRepository.findById(input) != null;
			}
		});

		// �l��ID�Ɋ֘A����ғ����X�g�̌���
		List<Work> workList = findWorkListByHRId(hrId);
		if (workList.isEmpty()) {
			return;
		}
		
		 // �ғ��󋵂�\��
		workListView.display(workList);
		
		// �폜����ғ���ID�̎擾
		String workId = console.acceptFromIdList(workList, "�폜�������ғ��󋵂̔ԍ�����͂��Ă��������B");

		if (console.confirm("���̏����폜���܂����H(Y �͂��@N ������)", "Y", "N")) {
			workRepository.delete(new WorkKey(hrId, Long.parseLong(workId)));
			console.display("�폜���܂����B"); 
		}
	}

	private List<Work> findWorkListByHRId(long hrId) {
		Work workExample = new Work();
		workExample.setHrId(hrId);
		return workRepository.findByExample(workExample);
	}

}
