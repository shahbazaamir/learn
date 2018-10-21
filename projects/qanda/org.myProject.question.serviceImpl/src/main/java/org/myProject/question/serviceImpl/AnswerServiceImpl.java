package org.myProject.question.serviceImpl;

import java.net.UnknownHostException;

import org.myProject.common.logger.CommonLogger;
import org.myProject.service.question.AnswerDetail;
import org.myProject.service.question.AnswerService;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class AnswerServiceImpl implements AnswerService{

	public void saveAnswerDetails(AnswerDetail answerDetail) {
			MongoClient mongoClient=null;
			try {
				mongoClient = new MongoClient( "localhost" , 27017 );
				DB db = mongoClient.getDB("questionDb");
				DBCollection table = db.getCollection("answer");
				Gson gson=new Gson();
				String answerJson=gson.toJson(answerDetail);
				DBObject document = (DBObject)JSON.parse(answerJson);
				table.insert(document);
				
			} catch (UnknownHostException e) {
				CommonLogger.errorTrace(e);
			}finally{
				mongoClient.close();
			}

		}

	public String getAnswerDetail(AnswerDetail answerDetail) {
		MongoClient mongoClient=null;
		String answerDetailJson=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("answer");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("questionId", answerDetail.getQuestionId());		
			searchQuery.put("subjectId", answerDetail.getSubjectId());
			searchQuery.put("answerId", answerDetail.getAnswerId());
			DBCursor cursor = table.find(searchQuery);

			while (cursor.hasNext()) {
				answerDetailJson=cursor.next().toString();
			}
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return answerDetailJson;
	}
	
	

		
	}


