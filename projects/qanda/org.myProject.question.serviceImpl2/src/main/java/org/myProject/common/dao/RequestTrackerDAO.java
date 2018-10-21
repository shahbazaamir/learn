package org.myProject.common.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.myProject.common.dao.util.DatabaseHelper;
import org.myProject.common.model.RequestTrack;

public class RequestTrackerDAO {

	public int saveRequest(String requestId,String status,int errorCode,String errorDesc){
		RequestTrack requestTrack=new RequestTrack();
		requestTrack.setRequestId(requestId);
		requestTrack.setErrorCode(errorCode);
		requestTrack.setStatus(status);
		requestTrack.setErrorDesc(errorDesc);
		return saveRequest(requestTrack);
	}

	private int saveRequest(RequestTrack requestTrack) {
		Session s=DatabaseHelper.getSessionFactory().getCurrentSession();
		try{
		Transaction t=s.beginTransaction();
		s.save(requestTrack);
		t.commit();
		return 1;
		}finally{
			DatabaseHelper.closeResources(s);
		}
	}
}
