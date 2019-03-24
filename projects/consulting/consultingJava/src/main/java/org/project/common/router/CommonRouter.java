package org.project.common.router;

import org.project.banking.Banking;
import org.project.common.module.ModuleManager;
import org.project.common.vo.CommonVO;
import org.project.common.web.CommonParams;
import org.project.login.bo.LoginBO;
import org.project.passwodManager.PasswordManager;
public class CommonRouter {

	public CommonVO route(CommonVO objCommonVO) {
		try{
		String isAjax = (String)objCommonVO.getParamHm().get("isAjax");
		if(isAjax != null && isAjax.equals("Y")){
			objCommonVO = ajaxRoute(objCommonVO);
		}else{
			objCommonVO = commonRoute(objCommonVO);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return objCommonVO ;
		
	}

	private CommonVO ajaxRoute(CommonVO objCommonVO) {
		int actionId = Integer.parseInt( (String)objCommonVO.getParamHm().get(CommonParams.ACTION_ID));
		switch(actionId){
		case 1 :
			PasswordManager passManager = new PasswordManager();
			objCommonVO = passManager.getUserDetail(objCommonVO);
			break ;
		case 2 :
			PasswordManager passManager1 = new PasswordManager();
			objCommonVO = passManager1.getUserDetails(objCommonVO);
			break ;
		case 5 :
			objCommonVO = moduleRoute(objCommonVO);
		}
		return objCommonVO ;
	}
	private CommonVO commonRoute(CommonVO objCommonVO) throws Exception{
		int actionId = Integer.parseInt( (String)objCommonVO.getParamHm().get(CommonParams.ACTION_ID));
		switch(actionId){
		case 1 :
			LoginBO obj = new LoginBO();
			objCommonVO = obj.validateLogin(objCommonVO);
			break ;
		case 2 :
			PasswordManager obj1 = new PasswordManager();
			objCommonVO = obj1.insertPassword(objCommonVO);
			break ;
		case 3 :
			PasswordManager obj2 = new PasswordManager();
			objCommonVO = obj2.updatePassword(objCommonVO);
			break ;
		case 4 :
			//CommonSessionBeanInterfaceRemote obj3 = (CommonSessionBeanInterfaceRemote) EJBManager.getEJBObject(EJBReferences.COMMON_EJB);
			//objCommonVO.setCommonRespHm(   obj3.entry(objCommonVO.getParamHm()));
			Banking objBanking = new Banking();
			objCommonVO = (CommonVO) objBanking.processRequest(objCommonVO);
 			break ;
		case 5 :
			objCommonVO = moduleRoute(objCommonVO);
		}
		return objCommonVO ;
	}
	public CommonVO moduleRoute (CommonVO objCommonVO){
		String moduleId = (String)objCommonVO.getParamHm().get(CommonParams.MODULE);
		switch(moduleId){
			case ModuleManager.BANKING :
				Banking objBanking = new Banking();
				objCommonVO = (CommonVO) objBanking.processRequest(objCommonVO);
	 			break ;
		}
		return objCommonVO;
	}
}
