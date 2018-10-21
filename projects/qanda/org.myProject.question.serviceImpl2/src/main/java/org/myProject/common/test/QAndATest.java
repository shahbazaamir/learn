package org.myProject.common.test;

import org.myProject.qAndA.QAndA;
import org.myProject.qAndA.QAndAVO;

public class QAndATest {
	public void testQAndA(QAndAVO objQAndAVO){
		QAndA objQAndA = new QAndA();
		if(objQAndAVO == null)
			objQAndAVO = new QAndAVO();
		//objQAndA.fetchNextQuestion(objQAndAVO);
		System.out.println(objQAndAVO.getNextQuestion().getQuestion());
		System.out.println(objQAndAVO.getNextQuestion().getOptionA());
	}
}
