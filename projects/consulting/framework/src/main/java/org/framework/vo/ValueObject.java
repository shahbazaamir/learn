package org.framework.vo;

import java.io.Serializable;

public abstract class ValueObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private String module ;
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	 
	

}
