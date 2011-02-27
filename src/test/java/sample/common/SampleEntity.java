package sample.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sample.common.entity.EntityBase;

public class SampleEntity extends EntityBase<Long> {

	private String name;
	private String age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	
	@Override
	public String[] toArray() {
			
		List<String> dataList = 
			new ArrayList<String>(
					Arrays.asList(
					String.valueOf(getId()),
					getName(),
					getAge()));
		
		dataList.addAll(createDateColumns());
		return dataList.toArray(new String[dataList.size()]);
	}

	@Override
	public void fromArray(String[] data) {
		int i = 0;

		setId(Long.parseLong(data[i++]));
		setName(data[i++]);
		setAge(data[i++]);
		
		readMetaCulumns(data, i);
	}

}
