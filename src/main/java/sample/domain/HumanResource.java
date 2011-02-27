package sample.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 人材エンティティ
 */
public class HumanResource extends Party {


	/** 誕生日 */
	private String birthDay;

	/** 性別 */
	private String genderType;

	/** 業種ID */
	private long occupationId;

	/** 経験年数 */
	private String yearOfExperience;

	/** 最終学歴 */
	private String schoolBackground;

	/** 希望単価 */
	private String requestedSalary;
	

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getGenderType() {
		return genderType;
	}

	public void setGenderType(String genderType) {
		this.genderType = genderType;
	}

	public long getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(long occupationId) {
		this.occupationId = occupationId;
	}

	public String getYearOfExperience() {
		return yearOfExperience;
	}

	public void setYearOfExperience(String yearOfExperience) {
		this.yearOfExperience = yearOfExperience;
	}

	public String getSchoolBackground() {
		return schoolBackground;
	}

	public void setSchoolBackground(String schoolBackground) {
		this.schoolBackground = schoolBackground;
	}

	public String getRequestedSalary() {
		return requestedSalary;
	}

	public void setRequestedSalary(String requestedSalary) {
		this.requestedSalary = requestedSalary;
	}

	// TODO
	// 以下の部分のコードはメタ情報からで自動生成できるはず

	@Override
	public String[] toArray() {
			
		List<String> dataList = 
			new ArrayList<String>(
					Arrays.asList(
							String.valueOf(getId()),
							getName(),
							getPostalCode(),
							getAddress(),
							getTelephoneNo(),
							getFaxNo(),
							getEmail(),
							getBirthDay(),
							getGenderType(),
							String.valueOf(getOccupationId()),
							getYearOfExperience(),
							getSchoolBackground(),
							getRequestedSalary()));
		
		dataList.addAll(createDateColumns());
		return dataList.toArray(new String[dataList.size()]);
	}

	
	public void fromArray(String[] data) {
		int i = 0;

		setId(Long.parseLong(data[i++]));
		setName(data[i++]);
		setPostalCode(data[i++]);
		setAddress(data[i++]);
		setTelephoneNo(data[i++]);
		setFaxNo(data[i++]);
		setEmail(data[i++]);
		setBirthDay(data[i++]);
		setGenderType(data[i++]);
		setOccupationId(Long.valueOf(data[i++]));
		setYearOfExperience(data[i++]);
		setSchoolBackground(data[i++]);
		setRequestedSalary(data[i++]);

		readMetaCulumns(data, i);
	}	

}
