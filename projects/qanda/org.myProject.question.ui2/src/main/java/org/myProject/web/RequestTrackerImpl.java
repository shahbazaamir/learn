package org.myProject.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myProject.common.constants.WebProjectConstant;

public class RequestTrackerImpl implements RequestTracker{
	public void startRequestTracking(HttpServletRequest request, HttpServletResponse arg1){
		//String requestId =generateRequestId();
		request.setAttribute(WebProjectConstant.REQUEST_ID, RequestSequence.getNextSequence());
	}

	public void saveRequestStatus(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Map m=request.getParameterMap();
		m.put(WebProjectConstant.REQUEST_KEY_ACTION_ID, "6");
	}

	

}
