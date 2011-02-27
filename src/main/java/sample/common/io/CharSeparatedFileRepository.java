package sample.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
 * 区切り文字を使ったテキストファイル上のデータを読み書きするためのレポジトリー実装クラスです。
 * このクラスはステートを持ちスレッドセーフではない点に注意すること。
 */
public class CharSeparatedFileRepository<K extends Comparable<K>, E extends EntityBase<K>> implements
		Repository<K, E> {

	// ====================================================
	// フィールド
	// ====================================================

	private File masterFile;

	private File workFile;

	private String separator = "\t";

	private BufferedReader reader;

	private BufferedWriter writer;

	private Class<E> entityClass;

	private final NonDeletedMatcher<E> ALL_MATCHER = NonDeletedMatcher.instance();

	private String encoding = "utf-8";
	
	// ====================================================
	// プロパティ
	// ====================================================

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

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
	// メソッド
	// ====================================================

	@PostConstruct
	@SuppressWarnings("unchecked")
	public void init() {
		if (entityClass != null) return;
		
		// 親クラスの総称パラメータの型を取得
		entityClass = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	private List<E> doFind(Matcher<E> matcher) {
		try {
			List<E> result = new ArrayList<E>();

			openForRead();
			String line;

			// マスタから1行ずつ読込み
			while ((line = reader.readLine()) != null) {
				E entity = toEntity(line);
				if (!entity.isLogicalDeleted() && matcher.isMatch(entity)) {

					result.add(entity);
				}
			}

			return result;

		} catch (IOException e) {
			throw new SystemException("検索処理実行時にIO例外が発生しました。", e);
		} finally {
			close();
		}
	}

	@Override
	public E findById(K id) {

		Matcher<E> idMatcher = new IdMatcher<K, E>(id);

		List<E> result = doFind(idMatcher);

		if (result.isEmpty()) {
			throw new EntityNotFoundException("id = " + id + "のエンティティは存在しません。");
		}

		// TODO 一意性チェックはしていない
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
			throw new SystemException("削除処理実行時にIO例外が発生しました。", e);
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
		if (data == null) throw new IllegalArgumentException("パラメーターが不正です。");

		processUpdate(new FileUpdator() {
			
			@Override
			public void handle() throws IOException {
				String line;

				List<K> idList = new ArrayList<K>();
				// マスタから1行ずつ読込み
				while ((line = reader.readLine()) != null) {
					E entity = toEntity(line);
					idList.add(entity.getId());

					writeEntity(entity);
				}

				K maxId = Collections.max(idList);
				data.setId(nextId(maxId));

				data.preCreate(); // 更新、作成日付の発行
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
			throw new IllegalArgumentException("自動採番できません。");
		}
	}

	
	@Override
	public void update(final E data) {
		if (data == null)
			throw new IllegalArgumentException("パラメーターが不正です。");
		if (!data.isPersisted())
			throw new IllegalArgumentException("パラメーターが永続化されていません。");

		processUpdate(new FileUpdator() {
			@Override
			public void handle() throws IOException {
				String line;

				// マスタから1行ずつ読込み
				while ((line = reader.readLine()) != null) {
					E entity = toEntity(line);
					if (data.getId().equals(entity.getId())) {
						if (entity.isLogicalDeleted()) { // 既に論理削除済みの場合
							throw new EntityNotFoundException("id = "
									+ entity.getId() + "のエンティティは既に論理削除されています。");
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

				// マスタから1行ずつ読込み
				while ((line = reader.readLine()) != null) {
					E entity = toEntity(line);

					if (ObjectUtils.equals(id, entity.getId())) {
						if (entity.isLogicalDeleted()) { // 既に論理削除済みの場合
							throw new EntityNotFoundException("id = " + id
									+ "のエンティティは既に論理削除されています。");
						}

						entity.logicalDelete();
						deleted = true;
					}

					writeEntity(entity);
				}

				if (!deleted) {
					// パラメーターで指定されたエンティティが存在しなかった場合
					throw new EntityNotFoundException("id = " + id
							+ "のエンティティは存在しません。");
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
			throw new SystemException("エンティティの復元時に例外が発生しました。", e);
		} catch (IllegalAccessException e) {
			throw new SystemException("エンティティの復元時に例外が発生しました。", e);
		}
	}

	protected String[] parseLine(String line) {
		StringTokenizer st = new StringTokenizer(line, getSeparator(), true);
		List<String> result = new ArrayList<String>();
		String prevToken = "";
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			
 			if (prevToken.equals(getSeparator()) && token.equals(getSeparator()) ) {
 				result.add(""); // 区切りが連続する場合は空文字をつめる。
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

			// テンポラリーファイルをマスタに置換え
			workFile.renameTo(masterFile);

		} catch (IOException e) {
			throw new SystemException("ワークファイルの変更をマスターファイルに反映できません。", e);
		}
	}

	// NOTE
	// 本来は全ファイルの内容をメモリ上に読み込んで処理したほうが簡単だが、
	// オリジナルの実装を極力残すことにした。

	private void openForWrite() throws IOException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(masterFile), getEncoding()));
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(workFile), getEncoding()));
	}

	private void openForRead() throws IOException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(masterFile), getEncoding()));
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
