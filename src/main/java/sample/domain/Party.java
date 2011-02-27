package sample.domain;

import sample.common.entity.EntityBase;
import sample.common.entity.NameId;

public abstract class Party extends EntityBase<Long> implements NameId<Long> {

	/** 氏名 */
	private String name;

	/** 郵便番号 */
	private String postalCode;

	/** 住所 */
	private String address;

	/** 電話番号 */
	private String telephoneNo;

	/** FAX番号 */
	private String faxNo;

	/** emailアドレス */
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

}
