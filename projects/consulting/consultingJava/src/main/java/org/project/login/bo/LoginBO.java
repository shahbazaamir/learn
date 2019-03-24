package org.project.login.bo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.framework.db.DBWrapper;
import org.project.common.vo.CommonVO;
import org.project.db.DBConManager;
import org.project.log.LogWriter;
import org.project.naming.JSPReference;
import org.project.user.UserVO;

public class LoginBO {
	
	public CommonVO validateLogin(CommonVO objCommonVO){
		Connection con = null ;
		ArrayList<Statement> stAL = null ;
		ArrayList<ResultSet> rsAL = null ;
		try{
			con = DBConManager.getConnection() ;
			stAL = new ArrayList<Statement>();
			rsAL = new ArrayList<ResultSet>();
			PreparedStatement ps = con.prepareStatement("SELECT USER_STATUS FROM P_USER_CONF WHERE USER_ID = ? AND USER_PASSWORD = ?  ");
			stAL.add(ps);
			ps.setString(1, (String) objCommonVO.getParamHm().get("userId"));
			ps.setString(2, (String) objCommonVO.getParamHm().get("password"));
			ResultSet rs =ps.executeQuery();
			UserVO uservo = new UserVO();
			while(rs.next()){
				if(rs.getString(1).equals("Y")){
					
					uservo.setUserStatus(true);
					objCommonVO.setUserStatus(true);
					
				}
				objCommonVO.setUservo(uservo);
			}
			if(objCommonVO.isUserStatus()){
				objCommonVO.setResponsePage(JSPReference.COMMON_HOME);
			}else{
				objCommonVO.setResponsePage(JSPReference.LOGIN_ERR);
			}
		}catch(Exception e){
			LogWriter.writeLog("PEX00000002");
			e.printStackTrace();
		}finally{
			DBWrapper.closeDB(con, stAL, rsAL);
		}
		return objCommonVO;
	}
	
	
	public void saveUser(CommonVO objCommonVO) {
		Connection con = null ;
		ArrayList<Statement> stAL = null ;
		try{
			con = DBConManager.getConnection() ;
			stAL = new ArrayList<Statement>();
			PreparedStatement ps = con.prepareStatement("INSERT INTO SESSION_MASTER (SESSION_ID , USER_ID , STATUS ) VALUES (?,?,?) ");
			ps.setString(1, "DUMMY_SID");
			ps.setString(2, (String) objCommonVO.getParamHm().get("assosiateId"));
			ps.setString(3, "Y");
			ps.executeUpdate();
		}catch(Exception e){
			LogWriter.writeLog("PEX00000001");
			e.printStackTrace();
		}finally{
			DBWrapper.closeDB(con, stAL, null);
		}

		
	}

}
