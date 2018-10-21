package org.myProject.common.util;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.myProject.common.constants.ProjectConfiguration;

public class ProjectUtil {
	private static ProjectUtil pu;
	private ProjectUtil(){}
	public static ProjectUtil getInstance(){
		if(pu==null){
			pu=new ProjectUtil();
		}
		return pu;
	}
	public Configuration getConfiguration(){
		Configuration cfg=null;
		if(ProjectConfiguration.ANNOTATION_SUPPORTED){
			cfg=	new AnnotationConfiguration();  
		}else{
			cfg=	new Configuration();
		}
	    return cfg.configure(ProjectConfiguration.CONFIG_FILE);
	}
}
