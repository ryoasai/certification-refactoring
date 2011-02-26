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
 * �l�ޏ����͏���
 */
@Component
public class InputHRFunction implements Function {

	@Inject
	private HumanResourceRepository hrRepository;

	@Inject
	private OccupationRepository occupationRespository;
	
	@Inject
	private Console console;
	
	private HumanResource selectedHumanResource;

	/**
	 * �l�ފǗ�(�ǉ�)�̎��s
	 */
	public void run() {
		
		selectedHumanResource = new HumanResource();
		selectedHumanResource.fromArray(inputData());
		
		hrRepository.create(selectedHumanResource);

		console.display("�l��ID�F" + selectedHumanResource.getId()
				+ " �œo�^����܂����B");
	}

	/**
	 * �l�ޏ��̓���
	 * 
	 * @param occupationList
	 *            �Ǝ탊�X�g��\��������z��
	 * @return ���͏��
	 */
	public String[] inputData() {
		String[] data = new String[HumanResourceView.FIELDS.length];
		
		for (int i = 1; i < HumanResourceView.FIELDS.length; i++) {
			if (HumanResourceView.FIELDS[i].equals("����")) {
				data[i] = console.accept(HumanResourceView.FIELDS[i] + "����͂��Ă��������B", new ValidInput<String>() {
					@Override
					public boolean isValid(String input) {
						return "M".equals(input) || "F".equals(input);
					}
				});
			
			} else if (HumanResourceView.FIELDS[i].equals("�Ǝ�")) {
				data[i] = console.acceptFromIdList(occupationRespository.findAll(), HumanResourceView.FIELDS[i] + "����͂��Ă��������B");
			} else {
				data[i] = console.accept(HumanResourceView.FIELDS[i] + "����͂��Ă��������B");
			}
		}
		
		return data;
	}
}
