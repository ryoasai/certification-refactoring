package sample.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sample.common.entity.EntityBase;
import sample.common.entity.NameId;

public class Occupation extends EntityBase<Long> implements NameId<Long> {


	/** �Ǝ햼 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String[] toArray() {
			
		List<String> dataList = 
			new ArrayList<String>(
					Arrays.asList(
							String.valueOf(getId()),
							getName()));
		
		dataList.addAll(createDateColumns());
		return dataList.toArray(new String[dataList.size()]);
	}

	
	public void fromArray(String[] data) {
		int i = 0;

		setId(Long.parseLong(data[i++]));
		setName(data[i++]);

		readMetaCulumns(data, i);
	}	
}
