package sample.domain;

import sample.common.entity.Sequence;

public class WorkKey implements Sequence<WorkKey>, Comparable<WorkKey> {

	long hrId;
	long workStatusId;
	

	public WorkKey(long hrId, long workStatusId) {
		this.hrId = hrId;
		this.workStatusId = workStatusId;
	}
	
	public long getHrId() {
		return hrId;
	}
	
	public long getWorkStatusId() {
		return workStatusId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof WorkKey)) return false;
 		
		WorkKey key = ((WorkKey)obj);
		
		return key.hrId == this.hrId 
			&& key.workStatusId == this.workStatusId;
	}

	@Override
	public int hashCode() {
		return (int)(hrId) ^ (int)workStatusId;
	}

	@Override
	public WorkKey next() {
		return new WorkKey(hrId, workStatusId + 1);
	}

	@Override
	public int compareTo(WorkKey o) {
		if (this.hrId > o.hrId) {
			return 1;
		} else if (this.hrId < o.hrId) {
			return -1;
		} else {
			if (this.workStatusId > o.workStatusId) {
				return 1;
			} else if (this.workStatusId < o.workStatusId) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(workStatusId);
	}
}
