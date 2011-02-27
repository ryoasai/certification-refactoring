package sample.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EntityBaseTest {

	SampleEntity sample = new SampleEntity();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testLogicalDelete() {
		assertFalse(sample.isLogicalDeleted());

		sample.logicalDelete();
		assertTrue(sample.isLogicalDeleted());
	}

	@Test(expected=IllegalStateException.class)
	public void testLogicalDelete_AlreadyDeleted() {
		assertFalse(sample.isLogicalDeleted());

		sample.logicalDelete();
		
		// 既に論理削除済みの場合例外となる。
		sample.logicalDelete();
	}

}
