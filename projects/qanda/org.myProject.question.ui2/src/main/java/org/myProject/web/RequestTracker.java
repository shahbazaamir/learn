package org.myProject.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestTracker {
	/**
	 * generates a request id and tracks all operations 
	 * @param arg0
	 * @param arg1
	 */
	public void startRequestTracking(HttpServletRequest arg0, HttpServletResponse arg1);

	public void saveRequestStatus(HttpServletRequest request,
			HttpServletResponse response);

}
