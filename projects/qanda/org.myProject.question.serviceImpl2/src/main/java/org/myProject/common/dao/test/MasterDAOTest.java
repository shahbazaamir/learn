package org.myProject.common.dao.test;

//import org.myProject.common.dao.MasterDAO;
import org.myProject.common.model.Subject;

public class MasterDAOTest {
	public void insert(){
		Subject s=new Subject();
		s.setSubjectCode("JPA");
		s.setSubjectDesc("Java Persistence API");
//		MasterDAO m =new MasterDAO();
//		m.add(s);
	}
	public static void main(String[] args) {
		new MasterDAOTest().insert();
	}
}
