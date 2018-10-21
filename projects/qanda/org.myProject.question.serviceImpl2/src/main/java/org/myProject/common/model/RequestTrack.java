package org.myProject.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="REQUEST_TRACK")
@NamedQuery(name="RequestTrack.findAll", query="SELECT q FROM RequestTrack q")
public class RequestTrack {

	@Column(name="REQUEST_ID")
	private String requestId;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="ERROR_CODE")
	private int errorCode;
	
	@Column(name="ERROR_DESC")
	private String errorDesc;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
}
