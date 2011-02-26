package sample.domain;

import sample.common.entity.EntityBase;
import sample.common.entity.NameId;

public abstract class Party extends EntityBase<Long> implements NameId<Long> {

	/** ���� */
	private String name;

	/** �X�֔ԍ� */
	private String postalCode;

	/** �Z�� */
	private String address;

	/** �d�b�ԍ� */
	private String telephoneNo;

	/** FAX�ԍ� */
	private String faxNo;

	/** email�A�h���X */
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
