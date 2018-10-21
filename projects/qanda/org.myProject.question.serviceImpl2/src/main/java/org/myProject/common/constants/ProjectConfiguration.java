package org.myProject.common.constants;

import java.util.ResourceBundle;

import org.myProject.common.dao.QAndADAO;

public class ProjectConfiguration {
	
	private static ResourceBundle rb;
	private static void  loadBundle(){
		if(rb==null)
		rb = ResourceBundle.getBundle("myProjectConfig");
			
	}
	
	private static String getProperty(String key){
		loadBundle();
		return rb.getString(key);
	}
	public static final String CONFIG_FILE =getProperty("configFile");
	public static final String DATASOURCE =getProperty("dataSource");
	public static final String CONFIG_TYPE =getProperty("configType");
	
	public static final boolean ANNOTATION_SUPPORTED=Boolean.parseBoolean(getProperty("annotationSupported")==null?"false":getProperty("annotationSupported"));
	public static final int PROJECT_ORM_TYPE = Integer.parseInt(getProperty("projectOrmType")==null?"0":getProperty("projectOrmType"));
	private static String version=getProperty("version");
	public static final int VERSION=Integer.parseInt(version==null ?"1":"".equals(version.trim())?"1":version);
}
