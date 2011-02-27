package sample.common.util;

import org.apache.commons.lang.ObjectUtils;

import sample.common.entity.EntityBase;

public class IdMatcher<K extends Comparable<K>, E extends EntityBase<K>> implements Matcher<E> {

	private K id;
	
	public IdMatcher(K id) {
		this.id = id;
	}
	
	@Override
	public boolean isMatch(E target) {
		return ObjectUtils.equals(id, target.getId());
	}
	
}
