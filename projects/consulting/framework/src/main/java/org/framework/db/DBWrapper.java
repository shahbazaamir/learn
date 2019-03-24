package org.framework.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.sql.DataSource;

import org.framework.config.ConfigVO;
import org.framework.naming.NamingVO;
import org.framework.naming.NamingWrapper;


public class DBWrapper {
	public static Connection getConnection(ConfigVO configValueObj)throws Exception {
		Connection con = null ; 
		try{
			NamingWrapper naming = new NamingWrapper();
			NamingVO objNamingVO = configValueObj.getNamingVO();
			Context context =  naming.doLookUp(objNamingVO);
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
	public static void closeDB(Connection con , ArrayList<Statement> stAL, ArrayList<ResultSet> rsAL ){
		if(rsAL != null ){
			for(ResultSet rs : rsAL){
				try{
					rs.close();
				}catch(Exception e){

				}
			}
		}
		if(stAL != null){
			for(Statement st : stAL){
				try{
					st.close();
				}catch(Exception e){

				}
			}
		}
		if(con != null ){
			try{
				con.close();
			}catch(Exception e ){

			}
		}
	}
}
