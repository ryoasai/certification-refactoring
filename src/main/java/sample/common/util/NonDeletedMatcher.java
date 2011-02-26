package sample.common.util;

import sample.common.entity.EntityBase;

public class NonDeletedMatcher<E extends EntityBase<?>> implements Matcher<E> {

	private static final NonDeletedMatcher<?> INSTANCE = new NonDeletedMatcher<EntityBase<Comparable<?>>>();
	
	@SuppressWarnings("unchecked")
	public static <E extends EntityBase<?>> NonDeletedMatcher<E> instance() {
		return (NonDeletedMatcher<E>)INSTANCE;
	} 
	
	private NonDeletedMatcher() {}
	
	@Override
	public boolean isMatch(E target) {
		return !target.isLogicalDeleted();
	}
	
}
