package org.myProject.common.dao.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.myProject.common.constants.ProjectConfiguration;
import org.myProject.common.constants.ProjectConstants;
import org.myProject.common.util.ProjectUtil;

public class DatabaseHelper {

      private static final SessionFactory sessionFactory;

      static {
          try {
              // Create the SessionFactory from hibernate.cfg.xml
              //sessionFactory = new Configuration().configure().buildSessionFactory();
        	  
        	  Configuration cfg=ProjectUtil.getInstance().getConfiguration();
      	     sessionFactory=cfg.buildSessionFactory();  
          } catch (Throwable ex) {
              // Make sure you log the exception, as it might be swallowed
              ex.printStackTrace();
              throw new ExceptionInInitializerError(ex);
          }
      }

      public static SessionFactory getSessionFactory() {
          return sessionFactory;
      }
      public static Session getSession() {
    	  SessionFactory sessionFactory=getSessionFactory();
    	  if(ProjectConstants.CONFIG_TYPE_STANDALONE.equals( ProjectConfiguration.CONFIG_TYPE)){
    		  return sessionFactory.openSession();
    	  }else{
    		  return sessionFactory.getCurrentSession();
    	  }
      }
      
      public static void closeResources(Session s){
    	  if(s!= null){
    		  try{
    			 s.close(); 
    		  }catch(Exception e){
    			  
    		  }
    	  }
      }

}