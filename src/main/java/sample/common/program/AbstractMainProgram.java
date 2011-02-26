package sample.common.program;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import sample.common.console.Console;

/**
 * �l�ޔh���Ǘ��v���O�������C���N���X
 */
public abstract class AbstractMainProgram extends AbstractDispatcher implements MainProgram {

	@Resource
	protected Map<String, Function> functionMap = new HashMap<String, Function>();

	@Autowired
	protected Console console;

	public Map<String, Function> getFunctionMap() {
		return functionMap;
	}

	public void setFunctionMap(Map<String, Function> functionMap) {
		this.functionMap = functionMap;
	}
	
	protected boolean isConfirm(String inputCode) {
		return !"S".equals(inputCode);
	}

	/**
	 * �@�\�ꗗ�Ƌ@�\�R�[�h�ꗗ��\�����C�@�\�R�[�h���擾���ĊY���̋@�\���Ăяo��
	 */
	@Override
	protected void runFunction(String inputCode) {
		Function subFunction = functionMap.get(inputCode);
		if (subFunction == null) return;
		
		assert subFunction != null;
		
		try {
			subFunction.run();
		} catch (Exception ex) {
			// TODO �K�؂ȗ�O����
			ex.printStackTrace();
		}
		
		if (isConfirm(inputCode)) { // �l�ފǗ��Ɖғ��󋵊Ǘ��̂�
			console.accept("�G���^�[�L�[�������ƃ��j���[�ɖ߂�܂��B");
		}
	}
}
