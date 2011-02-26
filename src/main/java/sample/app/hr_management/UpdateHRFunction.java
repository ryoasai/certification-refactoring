package sample.app.hr_management;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import sample.common.console.Console;
import sample.common.console.ValidInput;
import sample.common.program.Function;
import sample.domain.HumanResource;
import sample.repository.HumanResourceRepository;
import sample.repository.OccupationRepository;

/**
 * �l�ޏ��X�V
 */
@Component
public class UpdateHRFunction implements Function {

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private OccupationRepository occupationRespository;
	
	@Inject
	private HumanResourceView hrView;
	
	@Inject
	private Console console;
	
	private HumanResource selectedHumanResource;

	/**
	 * �l�ފǗ�(�X�V)���j���[�̎��s
	 */
	public void run() {
		selectHumanResource();
		
		int itemNo = inputItemNo();
		if (itemNo < 1 || itemNo > 12) { // ���ڔԍ����̓G���[
			console.display("���ڔԍ��̓��͂�����������܂���B");
			console.display("�X�V�ł��܂���ł����B");
			return;
		}

		inputData(itemNo);

		// ���͂��ꂽ�l���t�@�C���ɕۑ�
		hrRepository.update(selectedHumanResource);
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
	
	
	private int inputItemNo() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n�X�V���������ڂ���͂��Ă��������B\n");
		
		displayMenuItems(sb);

		return console.acceptInt(sb.toString());
	}

	private void displayMenuItems(StringBuilder buff) {
		for (int i = 1; i < HumanResourceView.FIELDS.length; i++) {
			buff.append(i + "." + HumanResourceView.FIELDS[i]);
			// TODO ���Ȃ�X�����W�b�N�����A����̃��W�b�N��ۑ����Ă����B
			// �{���̓^�u�ʒu��ėp�I�Ɏ����������郍�W�b�N�������ׂ�
			
			if (i == 1 || i == 8 || i == 9)
				buff.append("\t");
			if (i == 3 || i == 5 || i == 7 || i == 10 || i == 12)
				buff.append("\n");
			else if (i != 6)
				buff.append("\t");
		}
		
		buff.append("\n [1-12]>");
	}

	/**
	 * �l�ޏ��̓���
	 * 
	 * @param occupationList
	 *            �Ǝ탊�X�g��\��������z��
	 * @return ���͏��
	 */
	public void inputData(int itemNo) {
		String[] data = selectedHumanResource.toArray();

		if (itemNo == 8) {
			data[itemNo] = console.accept("�X�V��̒l����͂��Ă��������B(M�F�j�� F�F����)\n[M,F]", new ValidInput<String>() {
				@Override
				public boolean isValid(String input) {
					return "M".equals(input) || "F".equals(input);
				}
			});
		
		} else if (itemNo == 9) {
			data[itemNo] = console.acceptFromIdList(occupationRespository.findAll(), "�X�V��̒l����͂��Ă��������B");
		} else {
			data[itemNo] = console.accept("�X�V��̒l����͂��Ă��������B");
		}
	
		selectedHumanResource.fromArray(data);
	}

}
