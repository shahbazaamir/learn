package org.project.naming;

import javax.naming.InitialContext;

import org.framework.config.ConfigConstants;
import org.framework.config.ConfigVO;
import org.framework.naming.NamingVO;
import org.framework.naming.NamingWrapper;
import org.project.config.ConfigManager;

public class NamingManager {
	public static NamingVO getNamingVO(){
		ConfigVO configVO = ConfigManager.getConfigVO();
		NamingVO namingVO = new NamingVO();
		if(configVO.getAppServerName() == ConfigConstants.JBOSS_5){
			namingVO.setContextFactory("org.jnp.interfaces.NamingContextFactory");
			namingVO.setContextUrl("jnp://localhost:2012");
		}else if(configVO.getAppServerName() == ConfigConstants.JBOSS_AS_7){
			//namingVO.setContextFactory("org.jboss.naming.remote.client.InitialContextFactory");
			//namingVO.setContextUrl("remote://localhost:4447");
			namingVO.setContextPropRequired(false);
		}
		
		
		
		return namingVO ;
	}
	public static InitialContext getInitialContext()throws Exception{
		NamingVO objNamingVO =  getNamingVO();
		NamingWrapper obj = new NamingWrapper();
		return (InitialContext) obj.doLookUp(objNamingVO);
	}

}
