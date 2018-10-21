package org.myProject.common.web;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import javax.sql.DataSource;

import org.myProject.common.constants.WebProjectConstant;
import org.myProject.common.factory.WebProjectFactory;
import org.myProject.common.logger.Logger;
import org.myProject.web.RequestTracker;

public class CommonServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		doPost(arg0, arg1);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Logger.info("request reached");
		RequestTracker rt=null;
			//String module = request.getParameter("Module");
			Logger.info("lookup test ");
		 //Hashtable props = new Hashtable();
		 //props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		 //props.put("jboss.naming.client.ejb.context",true);
		 //props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		 //props.put(Context.PROVIDER_URL,"remote://localhost:4447");

         // create the InitialContext
         //Context context = new javax.naming.InitialContext();
         //OurEar/Application/ApplicationBean!org.application.ApplicationBeanRemote
         //ApplicationBeanRemote bean = (ApplicationBeanRemote) context.lookup("ejb:OurEar/Application/ApplicationBean!org.application.ApplicationBeanRemote");
         //ApplicationBeanRemote bean = (ApplicationBeanRemote) context.lookup("java:app/Application/ApplicationBean!org.application.ApplicationBeanRemote");
         //BankingRemote bean = (BankingRemote)context.lookup("java:app/BankingEJB/Banking!org.banking.bean.BankingRemote");
         try{
         //BankingRemote bean = (BankingRemote)context.lookup("java:app/BankingEJB/Banking!org.banking.bean.BankingRemote");
         
         //Logger.info("lookup done ");
         //bean.saveApplication();
        // Logger.info("data saved ");
        	 //DataSource ds=(DataSource)context.lookup("java:/banking");
        	 //ds.getConnection();
        	 CommonRouter objCommonRouter=new CommonRouter();
        	 if(WebProjectConstant.version >=2){
        		 if(WebProjectConstant.REQUEST_TRACKER_SUPPORTED){
        			  rt=WebProjectFactory.getRequestTracker();
        					 rt.startRequestTracking(request,response);
        		 }
        	 }
        	 objCommonRouter.route(request,response);
        	 if(WebProjectConstant.version >=2){
        		 if(WebProjectConstant.REQUEST_TRACKER_SUPPORTED){
        			 if(rt==null) rt=WebProjectFactory.getRequestTracker();
        					 rt.saveRequestStatus(request,response);
        		 }
        	 }
        	 
		}catch(Exception e){
			e.printStackTrace();
			 if(WebProjectConstant.version >=2){
        		 if(WebProjectConstant.REQUEST_TRACKER_SUPPORTED){
        			 
        			 if(rt==null)rt=WebProjectFactory.getRequestTracker();
        					 rt.saveRequestStatus(request,response);
        		 }
        	 }
		}
         
         
		  
	}

}
