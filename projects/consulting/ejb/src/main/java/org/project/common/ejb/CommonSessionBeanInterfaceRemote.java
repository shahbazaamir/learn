package org.project.common.ejb;

import java.util.HashMap;

import javax.ejb.Remote;


@Remote
public interface CommonSessionBeanInterfaceRemote {
	public HashMap<String,Object> entry(HashMap<String, Object> txnMap);
}
