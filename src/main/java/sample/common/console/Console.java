package sample.common.console;

import java.util.Date;
import java.util.List;

import sample.common.entity.Identifiable;
import sample.common.entity.NameId;

/**
 * �R���\�[���o�͏������J�v�Z�������邽�߂̃C���^�[�t�F�[�X
 */
public interface Console {

	/**
	 * ���b�Z�[�W��\������B
	 * @param messages �\���Ώۃ��b�Z�[�W
	 */
	void display(String... messages);

	/**
	 * YesNo�̑I�����b�Z�[�W��\������B
	 * @param message �\���Ώۃ��b�Z�[�W
	 * @return Yes���I�����ꂽ�ꍇ��true
	 */
	boolean confirm(String message, String yes, String no);
	
	/**
	 * ���b�Z�[�W�ƂƂ��ɓ��̓v�����v�g��\�����A�W�����͂���̓��͂��󂯕t����
	 * 
	 * @param �\�����b�Z�[�W
	 * @return ���͕�����
	 */
	String accept(String message);

	/**
	 * ���b�Z�[�W�ƂƂ��ɓ��̓v�����v�g��\�����A�W�����͂���̓��͂��󂯕t����B
	 * ���������͒l��������܂ŁA�ēx���͂��J��Ԃ��B
	 * 
	 * @param �\�����b�Z�[�W
	 * @return ���͕�����
	 */
	String accept(String message, ValidInput<String> validInput);

	/**
	 * ���b�Z�[�W�ƂƂ��ɓ��̓v�����v�g��\�����A�W�����͂���̐������͂��󂯕t����
	 * 
	 * @param �\�����b�Z�[�W
	 * @return ���͒l
	 */
	int acceptInt(String message);

	int acceptInt(String message, ValidInput<Integer> validInput);

	/**
	 * ���b�Z�[�W�ƂƂ��ɓ��̓v�����v�g��\�����A�W�����͂���̒��������͂��󂯕t����
	 * 
	 * @param �\�����b�Z�[�W
	 * @return ���͒l
	 */
	long acceptLong(String message);

	long acceptLong(String message, ValidInput<Long> validInput);
	
	/**
	 * yyyyMMdd�����œ��t����͂���B���������t�����͂����܂ŏ������J��Ԃ��B
	 * 
	 * @param message �\�����b�Z�[�W
	 * @return ���͂��ꂽ���t
	 */
	Date acceptDate(String message);

	/**
	 * ���t����͂���B���������t�����͂����܂ŏ������J��Ԃ��B
	 * 
	 * @param message �\�����b�Z�[�W
	 * @param format ���t�t�H�[�}�b�g
	 * @return ���͂��ꂽ���t
	 */
	Date acceptDate(String message, String format);

	/**
	 * ���b�Z�[�W�ƂƂ��ɑI�����̃��X�g��\������B
	 * �I�����ʂ�Ԃ��B�������I�����ʂ����͂����܂ŁA�����ōē��͂𑣂��B
	 * 
	 * @param selectList
	 * @param message
	 * @return �I������
	 */
	String acceptFromNameIdList(List<? extends NameId<?>> selectList, String message);

	String acceptFromIdList(List<? extends Identifiable<?>> selectList, String message);

	String acceptFromList(List<String> selectList, String message);
}
