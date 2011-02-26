package sample;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import sample.common.program.AbstractMainProgram;
import sample.common.program.MainProgram;

/**
 * ���C���v���O����
 */
@Component("mainProgram")
public class TempHRManagementProgram extends AbstractMainProgram {

	/**
	 * �@�\�ꗗ
	 */
	private static final String[] MENU_LIST = {
			"_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/",
			"            �l�ފǗ��V�X�e��",
			"                ���j���[",
			"  [1].�l�ތ���(S)",
			"  [2].�l�ފǗ�(JI�F�ǉ� JU�F�X�V JD�F�폜)",
			"  [3].�ғ��󋵊Ǘ�(KI�F�ǉ� KD�F�폜)",
			"  [4].�I��(E)",
			"_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/"};

	/**
	 * �@�\�R�[�h�ꗗ
	 */
	private static final List<String> CODE_LIST = Arrays.asList( "S", "JI", "JU", "JD", "KI", "KD", "E" );

	@Override
	protected String printMenuAndWaitForInput() {
		console.display(""); //���s
		console.display(MENU_LIST);
		return console.acceptFromList(CODE_LIST, "�ǂ̋@�\�����s���܂����H");
	}
	
	/**
	 * ���C���G���g���[�|�C���g
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/app-context.xml");
		
		MainProgram mainProgram = context.getBean("mainProgram", MainProgram.class);
		mainProgram.run();
	}

}
