package org.myProject.master;

public class MasterAdapter {
	public String save(Object o){
		Master m=new Master();
		boolean res= m.save(o);
		if(res){
			return "success";
		}else{
			return "failure";
		}
	}
}
