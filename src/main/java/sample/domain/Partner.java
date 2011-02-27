package sample.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Partner extends Party {

	/**
	 * URL
	 */
	private String url;

	/**
	 * 担当者
	 */
	private String personInCharge;

	/**
	 * 締め日
	 */
	private String cutoffDay;

	/**
	 * 支払い日
	 */
	private String paymentDay;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public String getCutoffDay() {
		return cutoffDay;
	}

	public void setCutoffDay(String cutoffDay) {
		this.cutoffDay = cutoffDay;
	}

	public String getPaymentDay() {
		return paymentDay;
	}

	public void setPaymentDay(String paymentDay) {
		this.paymentDay = paymentDay;
	}

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
							getUrl(),
							getPersonInCharge(),
							getEmail(),
							getCutoffDay(),
							getPaymentDay()));
		
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
		setUrl(data[i++]);
		setEmail(data[i++]);
		setPersonInCharge(data[i++]);
		setCutoffDay(data[i++]);
		setPaymentDay(data[i++]);

		readMetaCulumns(data, i);
	}	

}
