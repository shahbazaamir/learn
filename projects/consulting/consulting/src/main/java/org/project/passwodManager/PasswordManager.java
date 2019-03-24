package org.project.passwodManager;

import org.project.common.vo.CommonVO;
import org.project.naming.JSPReference;

public class PasswordManager {
	public CommonVO getUserDetail(CommonVO objCommonVO){
		PasswordManagerDAO objPMDAO= new PasswordManagerDAO();
		objCommonVO = objPMDAO.getUserDetail(objCommonVO);
		return objCommonVO;
	}
	public CommonVO getUserDetails(CommonVO objCommonVO){
		PasswordManagerDAO objPMDAO= new PasswordManagerDAO();
		objCommonVO = objPMDAO.getUserDetails(objCommonVO);
		return objCommonVO;
	}
	public CommonVO insertPassword(CommonVO objCommonVO){
		PasswordManagerDAO objPMDAO= new PasswordManagerDAO();
		int rowsUpdated  = objPMDAO.insertPassword(objCommonVO);
		/*
		switch(rowsUpdated){
		case -1 :
			rowsUpdated  = objPMDAO.updatePassword(objCommonVO);
			break;
		case 1 : 
			break;
		}
		*/
		objCommonVO.setResponsePage(JSPReference.PASSWORD_MANAGER_HOME);
		return objCommonVO;
	}
	public CommonVO updatePassword(CommonVO objCommonVO){
		PasswordManagerDAO objPMDAO= new PasswordManagerDAO();
		int rowsUpdated  = objPMDAO.updatePassword(objCommonVO);
		/*
		switch(rowsUpdated){
		case -1 :
			rowsUpdated  = objPMDAO.updatePassword(objCommonVO);
			break;
		case 1 : 
			break;
		}
		*/
		objCommonVO.setResponsePage(JSPReference.PASSWORD_MANAGER_HOME);
		return objCommonVO;
	}
}
