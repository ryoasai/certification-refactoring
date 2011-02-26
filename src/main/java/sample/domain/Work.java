package sample.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sample.common.entity.EntityBase;

/**
 * �ғ��󋵃G���e�B�e�B
 */
public class Work extends EntityBase<WorkKey> {

	/** �l��ID */
	private long hrId;
	
	private long workStatusNo;

	/** �����ID */
	private long partnerId;

	/** �ғ��J�n�� */
	private String startDate;

	/** �ғ��I���� */
	private String endDate;

	/** �_��P�� */
	private String contractSalary;

	@Override
	public void setId(WorkKey id) {
		super.setId(id);
		
		setHrId(id.getHrId());
		setWorkStatusNo(id.getWorkStatusId());
	}
	
	public long getHrId() {
		return hrId;
	}

	public void setHrId(long hrId) {
		this.hrId = hrId;
	}

	public long getWorkStatusNo() {
		return workStatusNo;
	}

	public void setWorkStatusNo(long workStatusNo) {
		this.workStatusNo = workStatusNo;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getContractSalary() {
		return contractSalary;
	}

	public void setContractSalary(String contractSalary) {
		this.contractSalary = contractSalary;
	}
	
	// TODO
	// �ȉ��̕����̃R�[�h�̓��^��񂩂�Ŏ��������ł���͂�
	
	@Override
	public String[] toArray() {
			
		List<String> dataList = 
			new ArrayList<String>(
					Arrays.asList(
							String.valueOf(getHrId()),
							String.valueOf(getWorkStatusNo()),
							String.valueOf(getPartnerId()),
							getStartDate(),
							getEndDate(),
							getContractSalary()));
		
		dataList.addAll(createDateColumns());
		return dataList.toArray(new String[dataList.size()]);
	}
	
	public void fromArray(String[] data) {
		int i = 0;
		setHrId(Long.valueOf(data[i++]));
		setWorkStatusNo(Long.valueOf(data[i++]));
		setPartnerId(Long.valueOf(data[i++]));
		setStartDate(data[i++]);
		setEndDate(data[i++]);
		setContractSalary(data[i++]);
		
		readMetaCulumns(data, i);
		
		setId(new WorkKey(getHrId(), getWorkStatusNo()));
	}

}
