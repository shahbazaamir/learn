package org.framework.config;

import org.framework.naming.NamingVO;
import org.framework.vo.ValueObject;

public class ConfigVO extends ValueObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NamingVO namingVO;
	private String dataSourceName ;
	private int appServerName ;
	public int getAppServerName() {
		return appServerName;
	}

	public void setAppServerName(int appServerName) {
		this.appServerName = appServerName;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public NamingVO getNamingVO() {
		return namingVO;
	}

	public void setNamingVO(NamingVO namingVO) {
		this.namingVO = namingVO;
	}
}
