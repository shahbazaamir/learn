package org.myProject.common.dao;

import java.util.List;
import java.util.Map;

import org.myProject.common.model.Question;

public interface QAndADAO {


	List loadQuestion(String quesionId);

	Map modifyQuestion(Map m);


	boolean addQuestion(Question q);

}
