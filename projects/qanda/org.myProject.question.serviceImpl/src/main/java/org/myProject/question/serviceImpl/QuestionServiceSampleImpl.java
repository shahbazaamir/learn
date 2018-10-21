package org.myProject.question.serviceImpl;

import java.net.UnknownHostException;

import org.myProject.common.logger.CommonLogger;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;


public class QuestionServiceSampleImpl implements QuestionService{

	public void saveQuestionDetails(QuestionDetail questionDetail) throws QuestionException {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");
			BasicDBObject document = new BasicDBObject();
			document.put("questionId", "1");
			document.put("question", "What is latest version of java");
			document.put("answer", "8");
			table.insert(document);
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}

	}

	public String getQuestionDetails( )
			throws QuestionException {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("questionId", "1");		
			DBCursor cursor = table.find(searchQuery);

			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}
		return null;
	}

	public String getQuestionDetail(QuestionDetail questionDetail)
			throws QuestionException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isQuestionDetailAvailable(QuestionDetail questionDetail)
			throws QuestionException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateQuestionDetailAvailable(QuestionDetail questionDetail)
			throws QuestionException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getQuestionDetailsBySubject(String subjectId)
			throws QuestionException {
		// TODO Auto-generated method stub
		return null;
	}

}
