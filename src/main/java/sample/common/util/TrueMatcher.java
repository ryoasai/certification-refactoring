package sample.common.util;

public class TrueMatcher<T> implements Matcher<T> {

	private static final TrueMatcher<?> INSTANCE = new TrueMatcher<Object>();
	
	@SuppressWarnings("unchecked")
	public static <T> TrueMatcher<T> instance() {
		return (TrueMatcher<T>)INSTANCE;
	} 
	
	private TrueMatcher() {}
	
	@Override
	public boolean isMatch(T target) {
		return true;
	}

}
