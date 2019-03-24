package org.framework.module;

import org.framework.vo.ValueObject;

/**
 * 
 * @author shahbaz.aamir
 *
 */
public interface Module {
	public ValueObject processRequest(ValueObject valueObject);

}
