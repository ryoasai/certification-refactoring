package sample.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

public class ExampleMatcher<T> implements Matcher<T> {

	private T example;
	
	public ExampleMatcher(T example) {
		if (example == null) throw new IllegalArgumentException();

		this.example = example;
	}
	
	public boolean isMatch(T target) {
		if (target == null) return false;
		
		PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(example.getClass());
		
		for (PropertyDescriptor prop : props) {
			Method readMethod = prop.getReadMethod();
			if (readMethod == null) continue;
			if (prop.getName().equals("class") || prop.getName().equals("persisted")) continue;
			
			Object exampleValue = ReflectionUtils.invokeMethod(readMethod, example);
			if (exampleValue == null) continue;
			if (exampleValue instanceof Long && (Long)(exampleValue) == 0) continue; // äÓñ{å^ÇÃlongÇÃ0ÇÕñ≥éãÅiÇ¢Ç‹Ç¢ÇøÅj

			Object targetValue = ReflectionUtils.invokeMethod(readMethod, target);

			if (targetValue instanceof String && exampleValue instanceof String) { // ïîï™ï∂éöóÒàÍív
				if ( ! ((String)targetValue).contains((String)exampleValue)) return false;
			} else {
				if (!ObjectUtils.equals(exampleValue, targetValue)) return false;
			}
		}
		
		return true;
	}
	
}
