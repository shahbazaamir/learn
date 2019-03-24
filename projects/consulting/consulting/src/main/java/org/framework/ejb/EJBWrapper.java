package org.framework.ejb;


public class EJBWrapper {
	/*
	public static Object getEJBObj(String EJBJndiName)throws Exception {
		NamingManager.
		try{
			NamingWrapper naming = new NamingWrapper();
			NamingVO objNamingVO = configValueObj.getNamingVO();
			Context context =  naming.(objNamingVO);
			con = ((DataSource)context.lookup(configValueObj.getDataSourceName())
					).getConnection();
			if(con == null)
				throw new Exception("Connection is null");
			//con = LoggingConnection.wrapIfNeeded(con);
			//CoreLogger.log("Connection:"+con);

		}
		catch(Exception e) {
			e.printStackTrace();
			throw e ;
		}
		return con ;
	} 
	*/  
}
