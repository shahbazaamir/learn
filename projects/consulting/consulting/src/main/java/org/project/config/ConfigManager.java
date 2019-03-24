package org.project.config;

import org.framework.config.ConfigConstants;
import org.framework.config.ConfigVO;

public class ConfigManager {
	public static ConfigVO getConfigVO(){
		ConfigVO configVO = new ConfigVO() ;
		configVO.setAppServerName(ConfigConstants.JBOSS_AS_7);
		return configVO ;
	}
}
