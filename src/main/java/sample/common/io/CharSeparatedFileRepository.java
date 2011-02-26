package sample.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import sample.common.SystemException;
import sample.common.entity.EntityBase;
import sample.common.entity.Sequence;
import sample.common.util.ExampleMatcher;
import sample.common.util.IdMatcher;
import sample.common.util.Matcher;
import sample.common.util.NonDeletedMatcher;

/**
 * ��؂蕶�����g�����e�L�X�g�t�@�C����̃f�[�^��ǂݏ������邽�߂̃��|�W�g���[�����N���X�ł��B
 * ���̃N���X�̓X�e�[�g�������X���b�h�Z�[�t�ł͂Ȃ��_�ɒ��ӂ��邱�ƁB
 */
public class CharSeparatedFileRepository<K extends Comparable<K>, E extends EntityBase<K>> implements
		Repository<K, E> {

	// ====================================================
	// �t�B�[���h
	// ====================================================

	private File masterFile;

	private File workFile;

	private String separator = "\t";

	private BufferedReader reader;

	private BufferedWriter writer;

	private Class<E> entityClass;

	private final NonDeletedMatcher<E> ALL_MATCHER = NonDeletedMatcher.instance();

	// ====================================================
	// �v���p�e�B
	// ====================================================

	public File getMasterFile() {
		return masterFile;
	}

	public void setMasterFile(File masterFile) {
		this.masterFile = masterFile;
	}

	public File getWorkFile() {
		return workFile;
	}

	public void setWorkFile(File workFile) {
		this.workFile = workFile;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	// ====================================================
	// ���\�b�h
	// ====================================================

	@PostConstruct
	@SuppressWarnings("unchecked")
	public void init() {
		if (entityClass != null) return;
		
		// �e�N���X�̑��̃p�����[�^�̌^���擾
		entityClass = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	private List<E> doFind(Matcher<E> matcher) {
		try {
			List<E> result = new ArrayList<E>();

			openForRead();
			String line;

			// �}�X�^����1�s���Ǎ���
			while ((line = reader.readLine()) != null) {
				E entity = toEntity(line);
				if (!entity.isLogicalDeleted() && matcher.isMatch(entity)) {

					result.add(entity);
				}
			}

			return result;

		} catch (IOException e) {
			throw new SystemException("�����������s����IO��O���������܂����B", e);
		} finally {
			close();
		}
	}

	@Override
	public E findById(K id) {

		Matcher<E> idMatcher = new IdMatcher<K, E>(id);

		List<E> result = doFind(idMatcher);

		if (result.isEmpty()) {
			throw new EntityNotFoundException("id = " + id + "�̃G���e�B�e�B�͑��݂��܂���B");
		}

		// TODO ��Ӑ��`�F�b�N�͂��Ă��Ȃ�
		return result.get(0);
	}

	@Override
	public List<E> findAll() {
		return doFind(ALL_MATCHER);
	}

	@Override
	public Map<K, E> findAllAsMap() {
		Map<K, E> result = new HashMap<K, E>();
		
		for (E entity: findAll()) {
			result.put(entity.getId(), entity);
		}
		
		return result;
	}

	
	@Override
	public List<E> findByExample(E example) {
		Matcher<E> exampleMatcher = new ExampleMatcher<E>(example);

		return doFind(exampleMatcher);
	}

	static interface FileUpdator {
		void handle() throws IOException;
	}

	private void processUpdate(FileUpdator fileUpdator) {
		try {
			openForWrite();

			fileUpdator.handle();

		} catch (IOException e) {
			throw new SystemException("�폜�������s����IO��O���������܂����B", e);
		} finally {
			close();
		}

		commit();
	}

	private void writeEntity(E data) throws IOException {
		String outputLine = fromEntity(data);
		writer.write(outputLine);
		writer.newLine();
	}

	@Override
	public void create(final E data) {
		if (data == null) throw new IllegalArgumentException("�p�����[�^�[���s���ł��B");

		processUpdate(new FileUpdator() {
			
			@Override
			public void handle() throws IOException {
				String line;

				List<K> idList = new ArrayList<K>();
				// �}�X�^����1�s���Ǎ���
				while ((line = reader.readLine()) != null) {
					E entity = toEntity(line);
					idList.add(entity.getId());

					writeEntity(entity);
				}

				K maxId = Collections.max(idList);
				data.setId(nextId(maxId));

				data.preCreate(); // �X�V�A�쐬���t�̔��s
				writeEntity(data);
			}

		});
	}

	@SuppressWarnings("unchecked")
	private K nextId(K maxId){
		if (maxId instanceof Long) {
			Object nextId = (Long)maxId + 1;
			return (K) nextId;
		} else if (maxId instanceof Sequence) {
			return ((Sequence<K>)maxId).next();
		} else {
			throw new IllegalArgumentException("�����̔Ԃł��܂���B");
		}
	}

	
	@Override
	public void update(final E data) {
		if (data == null)
			throw new IllegalArgumentException("�p�����[�^�[���s���ł��B");
		if (!data.isPersisted())
			throw new IllegalArgumentException("�p�����[�^�[���i��������Ă��܂���B");

		processUpdate(new FileUpdator() {
			@Override
			public void handle() throws IOException {
				String line;

				// �}�X�^����1�s���Ǎ���
				while ((line = reader.readLine()) != null) {
					E entity = toEntity(line);
					if (data.getId().equals(entity.getId())) {
						if (entity.isLogicalDeleted()) { // ���ɘ_���폜�ς݂̏ꍇ
							throw new EntityNotFoundException("id = "
									+ entity.getId() + "�̃G���e�B�e�B�͊��ɘ_���폜����Ă��܂��B");
						}

						data.preUpdate();
						entity = data;
					}

					writeEntity(entity);
				}
			}
		});
	}

	@Override
	public void delete(final K id) {
		processUpdate(new FileUpdator() {
			@Override
			public void handle() throws IOException {
				String line;
				boolean deleted = false;

				// �}�X�^����1�s���Ǎ���
				while ((line = reader.readLine()) != null) {
					E entity = toEntity(line);

					if (ObjectUtils.equals(id, entity.getId())) {
						if (entity.isLogicalDeleted()) { // ���ɘ_���폜�ς݂̏ꍇ
							throw new EntityNotFoundException("id = " + id
									+ "�̃G���e�B�e�B�͊��ɘ_���폜����Ă��܂��B");
						}

						entity.logicalDelete();
						deleted = true;
					}

					writeEntity(entity);
				}

				if (!deleted) {
					// �p�����[�^�[�Ŏw�肳�ꂽ�G���e�B�e�B�����݂��Ȃ������ꍇ
					throw new EntityNotFoundException("id = " + id
							+ "�̃G���e�B�e�B�͑��݂��܂���B");
				}
			}
		});
	}

	private String fromEntity(E entity) {
		return StringUtils.join(entity.toArray(), getSeparator());
	}

	private E toEntity(String line) {

		try {
			E entity = entityClass.newInstance();
			String[] data = parseLine(line);
			
			entity.fromArray(data);

			return entity;
		} catch (InstantiationException e) {
			throw new SystemException("�G���e�B�e�B�̕������ɗ�O���������܂����B", e);
		} catch (IllegalAccessException e) {
			throw new SystemException("�G���e�B�e�B�̕������ɗ�O���������܂����B", e);
		}
	}

	protected String[] parseLine(String line) {
		StringTokenizer st = new StringTokenizer(line, getSeparator(), true);
		List<String> result = new ArrayList<String>();
		String prevToken = "";
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			
 			if (prevToken.equals(getSeparator()) && token.equals(getSeparator()) ) {
 				result.add(""); // ��؂肪�A������ꍇ�͋󕶎����߂�B
 			} else if (!getSeparator().equals(token)) {
 				result.add(token);
 			}
 			
 			prevToken = token;
		}
		
		return result.toArray(new String[result.size()]);
	}

	private void commit() {
		try {
			if (!masterFile.delete()) {
				throw new IOException();
			}

			// �e���|�����[�t�@�C�����}�X�^�ɒu����
			workFile.renameTo(masterFile);

		} catch (IOException e) {
			throw new SystemException("���[�N�t�@�C���̕ύX���}�X�^�[�t�@�C���ɔ��f�ł��܂���B", e);
		}
	}

	// NOTE
	// �{���͑S�t�@�C���̓��e����������ɓǂݍ���ŏ��������ق����ȒP�����A
	// �I���W�i���̎������ɗ͎c�����Ƃɂ����B

	private void openForWrite() throws IOException {
		reader = new BufferedReader(new FileReader(masterFile));
		writer = new BufferedWriter(new FileWriter(workFile));
	}

	private void openForRead() throws IOException {
		reader = new BufferedReader(new FileReader(masterFile));
	}

	private void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
