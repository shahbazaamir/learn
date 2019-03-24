package org.framework.naming;

import java.util.Properties;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.framework.config.ConfigVO;



public class NamingWrapper {
	public Context doLookUp(NamingVO objNamingVO)throws Exception{
		Context cntxt = null;
		Properties prop = new Properties();
		try
		{
			if(objNamingVO.isContextPropRequired()){
				prop.put(Context.INITIAL_CONTEXT_FACTORY,objNamingVO.getContextFactory());
				prop.put(Context.PROVIDER_URL,objNamingVO.getContextUrl());
				if(objNamingVO.getUserName() != null ){
					prop.put(Context.SECURITY_PRINCIPAL,objNamingVO.getUserName());
				}
				if(objNamingVO.getPassword() != null){
					prop.put(Context.SECURITY_CREDENTIALS,objNamingVO.getPassword());
				}
				cntxt = new InitialContext(prop);
			}else{
				cntxt = new InitialContext();
			}

		}

		catch(CommunicationException cex)
		{
			cex.printStackTrace();
		}
		catch(NamingException nex){
			//LogWriter.writeLog("if JBAS011843 then change context factory if using Jboss 7");
			nex.printStackTrace();
			cntxt = new InitialContext();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		return cntxt;
	}
	
}
