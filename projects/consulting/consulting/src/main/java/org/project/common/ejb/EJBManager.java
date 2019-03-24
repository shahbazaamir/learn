package org.project.common.ejb;

import javax.naming.InitialContext;

import org.project.naming.NamingManager;

public class EJBManager {
	
	public static Object getEJBObject (String jndiName) throws Exception{
		InitialContext context = NamingManager.getInitialContext();
		Object ejbObj = context.lookup(jndiName);
		return ejbObj ;
	}
}
