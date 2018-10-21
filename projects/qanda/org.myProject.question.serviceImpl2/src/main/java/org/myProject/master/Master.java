package org.myProject.master;

import java.util.List;

import org.hibernate.Session;
import org.myProject.common.dao.DAOFactory;
import org.myProject.common.dao.MasterDAO;
import org.myProject.common.dao.util.DatabaseHelper;

public class Master {
	public boolean save (Object o){
		Session s =null;
		boolean status=false;
		try{
			MasterDAO dao=(MasterDAO)DAOFactory.getDAO(DAOFactory.MASTER_DAO);
			status=dao.save(o);
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DatabaseHelper.closeResources(s);
		}
		return 	status;
	}
}
