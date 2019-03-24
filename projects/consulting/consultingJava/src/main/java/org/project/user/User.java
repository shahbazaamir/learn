package org.project.user;

import model.PUser;

import org.framework.module.Module;
import org.framework.vo.ValueObject;
import org.project.banking.BankingBeanRemote;
import org.project.common.ejb.EJBManager;
import org.project.common.vo.CommonVO;
import org.project.common.web.CommonParams;
import org.project.log.LogWriter;
import org.project.naming.EJBReferences;
import org.project.naming.JSPReference;

public class User implements Module{
	public static final String WELCOME = "welcome";
	public ValueObject processRequest(ValueObject valueObject) {
		LogWriter.writeLog("User IN ::");
		CommonVO objCommonVO = (CommonVO) valueObject ;
		try{
			String reqId = (String)objCommonVO.getParamHm().get(CommonParams.REQUEST_ID);
			switch(reqId){
			case WELCOME :
				objCommonVO =welcomeUser(objCommonVO);
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
			objCommonVO.setResponsePage(JSPReference.USER_ERR);
		}finally{
			LogWriter.writeLog("User OUT ::");
		}
		return objCommonVO;
	}
	private CommonVO welcomeUser(CommonVO objCommonVO) throws Exception {
		UserBeanRemote userBean = (UserBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		PUser user = new PUser();
		user.setUserId(System.getProperty("user.name"));
		user = userBean.findUser(user);
		//if()
		
		//user.s
		return null;
	}
	
}
