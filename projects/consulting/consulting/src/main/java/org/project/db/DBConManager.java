package org.project.db;

import java.sql.Connection;

import org.framework.config.ConfigVO;
import org.framework.db.DBWrapper;
import org.framework.naming.NamingVO;
import org.project.naming.NamingManager;

public class DBConManager {
	public static Connection getConnection () throws Exception {
		NamingVO namingVO =  NamingManager.getNamingVO();
		ConfigVO configValueObj = new ConfigVO();
		configValueObj.setNamingVO(namingVO);
		configValueObj.setDataSourceName("java:/consultingDS");
		Connection con = DBWrapper.getConnection(configValueObj);
		return con ;
	}

}
