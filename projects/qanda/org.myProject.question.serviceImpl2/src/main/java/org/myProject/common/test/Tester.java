package org.myProject.common.test;

import org.myProject.qAndA.QAndA;
import org.myProject.qAndA.QAndAVO;

public class Tester {
public static void main(String[] args) {
	//HibernateTest.testHbn();
	//org.hibernate.dialect.Oracle10gDialect
	//QAndA qAndA=new QAndA();
	//QAndAVO qAndAVO=new QAndAVO();
	//qAndA.fetchNextQuestion(qAndAVO);
	//System.out.println(qAndAVO);
	QAndATest qAndATest=new QAndATest();
	QAndAVO objQAndAVO = new QAndAVO();
	objQAndAVO.setQuestionId("2");
	qAndATest.testQAndA(objQAndAVO);
}
}
