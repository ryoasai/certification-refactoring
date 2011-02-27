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
			if (exampleValue instanceof Long && (Long)(exampleValue) == 0) continue; // 基本型のlongの0は無視（いまいち）

			Object targetValue = ReflectionUtils.invokeMethod(readMethod, target);

			if (targetValue instanceof String && exampleValue instanceof String) { // 部分文字列一致
				if ( ! ((String)targetValue).contains((String)exampleValue)) return false;
			} else {
				if (!ObjectUtils.equals(exampleValue, targetValue)) return false;
			}
		}
		
		return true;
	}
	
}
