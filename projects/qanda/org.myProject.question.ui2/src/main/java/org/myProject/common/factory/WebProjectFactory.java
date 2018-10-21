package org.myProject.common.factory;

import org.myProject.web.RequestTracker;
import org.myProject.web.RequestTrackerImpl;

public class WebProjectFactory {
	public static RequestTracker getRequestTracker(){
		return new RequestTrackerImpl();
	}
}
