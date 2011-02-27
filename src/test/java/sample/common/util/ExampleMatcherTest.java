package sample.common.util;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sample.common.SampleEntity;

public class ExampleMatcherTest {

	
	ExampleMatcher<SampleEntity> target;
	
	SampleEntity example = new SampleEntity();
	SampleEntity sample = new SampleEntity();
	
	@Before
	public void setUp() throws Exception {
		target = new ExampleMatcher<SampleEntity>(example);

		sample.setName("test");
		sample.setAge("30");
		sample.setId(1L);
	}


	@Test
	public void isMatch_TargetIsNull() {
		assertFalse(target.isMatch(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void new_ExampleIsNull() {
		new ExampleMatcher<SampleEntity>(null);
	}

	@Test
	public void isMatch_True() {
		example.setName("test");
		
		assertTrue(target.isMatch(sample));
	}

	@Test
	public void isMatch_False() {
		example.setName("te*st");
		
		assertFalse(target.isMatch(sample));
	}
	
	@Test
	public void isMatch_PertialMatch() {
		example.setName("tes");
		
		assertTrue(target.isMatch(sample));
	}

}
