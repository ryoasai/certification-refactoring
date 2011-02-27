package sample.common.io;


import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sample.common.SampleEntity;

public class CharSeparatedFilePersisterTest {

	// FIXME 実行時のワークディレクトリーがワークスペースのルートである前提
	private static final String SP = SystemUtils.FILE_SEPARATOR;
	private static final String DATA_DIR = SystemUtils.USER_DIR + SP + "target" + SP + "test-classes";

	CharSeparatedFileRepository<Long, SampleEntity> target = new CharSeparatedFileRepository<Long, SampleEntity>();

	File masterFile;
	File workFile;
	
	@Before
	public void setUp() throws Exception {
		// テスト実行後に復元しやすいようにもともとのデータをコピーしてから操作する。
		File originalFile = new File(DATA_DIR, "sample.txt");;
		
		masterFile = new File(DATA_DIR, "test.txt");
		workFile = new File(DATA_DIR, "test.tmp");

		FileUtils.copyFile(originalFile, masterFile); 

		
		target.setMasterFile(masterFile);
		target.setWorkFile(workFile);
		target.setEntityClass(SampleEntity.class);
	}

	@After
	public void tearDown() throws Exception {
//		masterFile.delete();
//		tempFile.delete();
	}

	//====================================================
	// 検索関連
	//====================================================

	@Test
	public void findAll() {
		List<SampleEntity> sampleList = target.findAll();
		assertEquals(2, sampleList.size());
		
		assertEquals(1L, (long)sampleList.get(0).getId());
		assertEquals("20", sampleList.get(0).getAge());
		assertEquals("Name1", sampleList.get(0).getName());

		assertEquals(3L, (long)sampleList.get(1).getId());
		assertEquals("40", sampleList.get(1).getAge());
		assertEquals("Name3", sampleList.get(1).getName());
	}


	@Test
	public void findById() {
		SampleEntity sample = target.findById(1L);
		
		assertEquals(1L, (long)sample.getId());
		assertEquals("20", sample.getAge());
		assertEquals("Name1", sample.getName());
	}
	
	@Test
	public void findById_LogicalDeleted() {
		try {
			target.findById(2L);
		} catch (EntityNotFoundException ex) {
			assertEquals("id = 2のエンティティは存在しません。", ex.getMessage());
		}	
	}
	
	@Test
	public void findById_NotFound() {
		try {
			target.findById(100L);
		} catch (EntityNotFoundException ex) {
			assertEquals("id = 100のエンティティは存在しません。", ex.getMessage());
		}	
	}

	@Test
	public void findByExample() {
		SampleEntity example = new SampleEntity();
		example.setAge("20");
		
		List<SampleEntity> sampleList = target.findByExample(example);

		assertEquals(1, sampleList.size());

		assertEquals(1L, (long)sampleList.get(0).getId());
		assertEquals("20", sampleList.get(0).getAge());
		assertEquals("Name1", sampleList.get(0).getName());
	}

	//====================================================
	// 作成関連
	//====================================================
	
	@Test
	public void create_Normal() {
		SampleEntity example = new SampleEntity();
		example.setAge("50");
		example.setName("test");
		
		target.create(example);
		
		List<SampleEntity> entityList = target.findAll();
		assertEquals(3, entityList.size());
		
		SampleEntity addedEntity = entityList.get(entityList.size() - 1);
		assertEquals(4L, (long)addedEntity.getId());
		assertEquals("50", addedEntity.getAge());
		assertEquals("test", addedEntity.getName());
		
		assertNotNull(addedEntity.getCreateDate());
		assertNotNull(addedEntity.getUpdateDate());
	}
	
	//====================================================
	// 更新関連
	//====================================================
	
	@Test
	public void update_Normal() {
		SampleEntity sample = new SampleEntity();
		sample.setId(3L);
		sample.setAge("10");
		sample.setName("test");
		sample.setCreateDate(new Date());
		
		target.update(sample);
		
		SampleEntity updatedEntity = target.findById(3L);
		
		assertEquals(3L, (long)updatedEntity.getId());
		assertEquals("10", updatedEntity.getAge());
		assertEquals("test", updatedEntity.getName());
		assertNotNull(updatedEntity.getCreateDate());
		assertNotNull(updatedEntity.getUpdateDate());
	}

	@Test
	public void update_AlreadyLogicalDeleted() {
		SampleEntity sample = new SampleEntity();
		sample.setId(2L);
		sample.setAge("10");
		sample.setName("test");
		sample.setCreateDate(new Date());
		
		try {
			target.update(sample);
		} catch (EntityNotFoundException ex) {
			assertEquals("エンティティは既に論理削除されています。", ex.getMessage());
		}
	}
	
	//====================================================
	// 削除関連
	//====================================================

	@Test
	public void delete_Normal() {
		target.delete(1L);
	}
	
	@Test
	public void delete_AlreadyLogicalDeleted() {
		try {
			target.delete(2L);
		} catch (EntityNotFoundException ex) {
			assertEquals("id = 2のエンティティは既に論理削除されています。", ex.getMessage());
			
		}
	}
	
	@Test
	public void delete_NotFound() {
		try {
			target.delete(100L);
		} catch (EntityNotFoundException ex) {
			assertEquals("id = 100のエンティティは存在しません。", ex.getMessage());
		}
	}

}
