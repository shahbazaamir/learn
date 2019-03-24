package org.project.passwodManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.framework.db.DBConstants;
import org.framework.db.DBWrapper;
import org.project.common.vo.CommonVO;
import org.project.db.DBConManager;
import org.project.log.LogWriter;

public class PasswordManagerDAO {
	public CommonVO getUserDetail(CommonVO objCommonVO){
		Connection con = null ;
		ArrayList<Statement> stAL = null ;
		ArrayList<ResultSet> rsAL = null ;
		try{
			con = DBConManager.getConnection() ;
			stAL = new ArrayList<Statement>();
			rsAL = new ArrayList<ResultSet>();
			PreparedStatement ps = con.prepareStatement(" SELECT APP_CODE , APP_NAME  , APP_EMAIL , APP_USER_ID  , APP_PASSWORD FROM PASSWORD_MANAGER WHERE APP_CODE = ?   ");
			stAL.add(ps);
			ps.setString(1, (String) objCommonVO.getParamHm().get("appCode"));
			ResultSet rs =ps.executeQuery();
			rsAL.add(rs);
			HashMap<String, String> objHm = new HashMap<String, String>();
			while(rs.next()){
					objHm.put("APP_CODE", rs.getString(1));
					objHm.put("APP_NAME", rs.getString(2));
					objHm.put("APP_EMAIL", rs.getString(3));
					objHm.put("APP_USER_ID", rs.getString(4));
					objHm.put("APP_PASSWORD", rs.getString(5));
					objCommonVO.setAjaxResp(objHm);
			}
		}catch(Exception e){
			LogWriter.writeLog("PEX00000002");
			e.printStackTrace();
		}finally{
			DBWrapper.closeDB(con, stAL, rsAL);
		}
		return objCommonVO;
	}
	public CommonVO getUserDetails(CommonVO objCommonVO){
		Connection con = null ;
		ArrayList<Statement> stAL = null ;
		ArrayList<ResultSet> rsAL = null ;
		try{
			con = DBConManager.getConnection() ;
			stAL = new ArrayList<Statement>();
			rsAL = new ArrayList<ResultSet>();
			PreparedStatement ps = con.prepareStatement(" SELECT APP_CODE , APP_NAME  , APP_EMAIL , APP_USER_ID  , APP_PASSWORD FROM PASSWORD_MANAGER WHERE APP_CODE LIKE '%' || ? || '%'   ");
			stAL.add(ps);
			ps.setString(1, (String) objCommonVO.getParamHm().get("appCode"));
			ResultSet rs =ps.executeQuery();
			rsAL.add(rs);
			ArrayList<HashMap<String , String>> al = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> objHm = null ;
			while(rs.next()){
				objHm =new HashMap<String, String>();
					objHm.put("APP_CODE", rs.getString(1));
					objHm.put("APP_NAME", rs.getString(2));
					objHm.put("APP_EMAIL", rs.getString(3));
					objHm.put("APP_USER_ID", rs.getString(4));
					objHm.put("APP_PASSWORD", rs.getString(5));
					al.add(objHm);
					objCommonVO.setAjaxResp(al);
			}
		}catch(Exception e){
			LogWriter.writeLog("PEX00000002");
			e.printStackTrace();
		}finally{
			DBWrapper.closeDB(con, stAL, rsAL);
		}
		return objCommonVO;
	}
	public int insertPassword(CommonVO objCommonVO){
		Connection con = null ;
		ArrayList<Statement> stAL = null ;
		ArrayList<ResultSet> rsAL = null ;
		int rowsUpdated = -2 ;
		try{
			con = DBConManager.getConnection() ;
			stAL = new ArrayList<Statement>();
			rsAL = new ArrayList<ResultSet>();
			PreparedStatement ps = con.prepareStatement(" INSERT INTO PASSWORD_MANAGER ( APP_CODE , APP_NAME , APP_EMAIL , APP_USER_ID  , APP_PASSWORD ) VALUES ( ? , ? , ? , ? , ? )   ");
			stAL.add(ps);
			ps.setString(1, (String) objCommonVO.getParamHm().get("appCode"));
			ps.setString(2, (String) objCommonVO.getParamHm().get("appName"));
			ps.setString(3, (String) objCommonVO.getParamHm().get("appEmail"));
			ps.setString(4, (String) objCommonVO.getParamHm().get("appUserId"));
			ps.setString(5, (String) objCommonVO.getParamHm().get("appPassword"));
			rowsUpdated = ps.executeUpdate();
		}catch(SQLException e2){
			objCommonVO.getCommonRespHm().put(DBConstants.DB_ERROR_CODE,e2.getErrorCode());
			LogWriter.writeLog(e2.getErrorCode());
			if(e2.getErrorCode() == 1 ){
				rowsUpdated =-1 ;
			}
			LogWriter.writeLog("PEX00000005");
			e2.printStackTrace();
		}catch(Exception e){
			LogWriter.writeLog("PEX00000003");
			e.printStackTrace();
		}finally{
			DBWrapper.closeDB(con, stAL, rsAL);
		}
		return rowsUpdated ;
	}
	
	public int updatePassword(CommonVO objCommonVO){
		Connection con = null ;
		ArrayList<Statement> stAL = null ;
		ArrayList<ResultSet> rsAL = null ;
		int rowsUpdated = -1 ;
		try{
			con = DBConManager.getConnection() ;
			stAL = new ArrayList<Statement>();
			rsAL = new ArrayList<ResultSet>();
			PreparedStatement ps = con.prepareStatement(" UPDATE PASSWORD_MANAGER SET  APP_NAME = ? , APP_EMAIL = ?  , APP_USER_ID = ?  , APP_PASSWORD = ? WHERE APP_CODE = ?   ");
			stAL.add(ps);
			ps.setString(1, (String) objCommonVO.getParamHm().get("appName"));
			ps.setString(2, (String) objCommonVO.getParamHm().get("appEmail"));
			ps.setString(3, (String) objCommonVO.getParamHm().get("appUserId"));
			ps.setString(4, (String) objCommonVO.getParamHm().get("appPassword"));
			ps.setString(5, (String) objCommonVO.getParamHm().get("appCode"));
			rowsUpdated = ps.executeUpdate();
		}catch(Exception e){
			LogWriter.writeLog("PEX00000004");
			e.printStackTrace();
		}finally{
			DBWrapper.closeDB(con, stAL, rsAL);
		}
		return rowsUpdated ;
	}
}
