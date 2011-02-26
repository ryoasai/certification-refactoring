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
		
		// ���ɘ_���폜�ς݂̏ꍇ��O�ƂȂ�B
		sample.logicalDelete();
	}

}
