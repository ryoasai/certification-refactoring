package sample.common.io;

import java.util.List;
import java.util.Map;

import sample.common.entity.EntityBase;

/**
 * �G���e�B�e�B���i�������邽�߂̃��|�W�g���[�N���X���������ׂ��C���^�[�t�F�[�X�ł��B
 *
 * @param <E> �G���e�B�e�B�ɑ΂��鑍�̌^�p�����[�^�[
 */
public interface Repository<K extends Comparable<K>, E extends EntityBase<K>> {

	/**
	 * ID�ŃG���e�B�e�B����������B�i�_���폜�ς݂̃G���e�B�e�B�͏��O����B�j
	 * �G���e�B�e�B��������Ȃ��ꍇ��EntityNotFoundException�����o�����B
	 * 
	 * @param id ID
	 * @return �������ʂ̃G���e�B�e�B
	 * @throws EntityNotFoundException �G���e�B�e�B��������Ȃ��ꍇ
	 */
	E findById(K id);

	/**
	 * �G���e�B�e�B��S����������B�i�_���폜�ς݂̃G���e�B�e�B�͏��O����B�j
	 * �G���e�B�e�B��������Ȃ��ꍇ�͋��List���Ԃ�B
	 * 
	 * @return �G���e�B�e�B�̃��X�g
	 * @throws EntityNotFoundException �G���e�B�e�B��������Ȃ��ꍇ
	 */
	List<E> findAll();

	/**
	 * �G���e�B�e�B��S����������B�i�_���폜�ς݂̃G���e�B�e�B�͏��O����B�j
	 * �G���e�B�e�B��ID���L�[�Ƃ���}�b�v��Ԃ��B
	 * 
	 * @return �G���e�B�e�B��ID���L�[�Ƃ���}�b�v
	 * @throws EntityNotFoundException �G���e�B�e�B��������Ȃ��ꍇ
	 */
	Map<K, E> findAllAsMap();

	/**
	 * �G���e�B�e�B�̑����l���ƍ����Č�������
	 * @param example �����Ώۂ̑����l���i�[�����I�u�W�F�N�g
	 * @return �������ʂɈ�v�����G���e�B�e�B�̃��X�g
	 */
	List<E> findByExample(E example);

	/**
	 * �G���e�B�e�B��V�K�쐬����B
	 * @param data �쐬�Ώۂ̃G���e�B�e�B
	 */
	void create(E data);

	/**
	 * �G���e�B�e�B���X�V����B
	 * @param data �X�V�Ώۂ̃G���e�B�e�B
	 */
	void update(E data);

	/**
	 * �G���e�B�e�B��_���폜����B
	 * @param data �_���폜�Ώۂ̃G���e�B�e�B
	 */
	void delete(K id);

}
