package org.myProject.question.serviceImpl;

import java.net.UnknownHostException;

import org.myProject.common.logger.CommonLogger;
import org.myProject.service.question.QuestionDetail;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.QuestionService;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;


public class QuestionServiceImplOld implements QuestionService{

	public void saveQuestionDetails(QuestionDetail questionDetail) throws QuestionException {
		MongoClient mongoClient=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");
			Gson gson=new Gson();
			String questionJson=gson.toJson(questionDetail);
			DBObject document = (DBObject)JSON.parse(questionJson);
			table.insert(document);
			
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}

	}

	public String getQuestionDetails( )
			throws QuestionException {
		MongoClient mongoClient=null;
		String questionDetails=null;
		StringBuilder sb;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");

			BasicDBObject searchQuery = new BasicDBObject();
			//searchQuery.put("questionId", "1");		
			DBCursor cursor = table.find(searchQuery);
			sb=new StringBuilder(200);
			sb.append("[");
			while (cursor.hasNext()) {
				sb.append(cursor.next().toString()).append(",");
			}
			sb=new StringBuilder(sb.substring(0, sb.length()-1));
			sb.append("]");
			questionDetails=sb.toString();
			
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return questionDetails;
	}
	
	public String getQuestionDetailsBySubject( String subjectId)
			throws QuestionException {
		MongoClient mongoClient=null;
		String questionDetails=null;
		StringBuilder sb;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("subjectId", subjectId);		
			DBCursor cursor = table.find(searchQuery);
			sb=new StringBuilder(200);
			sb.append("[");
			while (cursor.hasNext()) {
				sb.append(cursor.next().toString()).append(",");
			}
			sb=new StringBuilder(sb.substring(0, sb.length()-1));
			sb.append("]");
			questionDetails=sb.toString();
			
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return questionDetails;
	}

	public String getQuestionDetail(QuestionDetail questionDetail)
			throws QuestionException {
		MongoClient mongoClient=null;
		String questionDetailJson=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("questionId", questionDetail.getQuestionId());		
			searchQuery.put("subjectId", questionDetail.getSubjectId());
			DBCursor cursor = table.find(searchQuery);

			while (cursor.hasNext()) {
				questionDetailJson=cursor.next().toString();
			}
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return questionDetailJson;
	}
	
	public boolean isQuestionDetailAvailable(QuestionDetail questionDetail)
			throws QuestionException {
		MongoClient mongoClient=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("questionId", questionDetail.getQuestionId());		
			searchQuery.put("subjectId", questionDetail.getSubjectId());
			DBCursor cursor = table.find(searchQuery);

			while (cursor.hasNext()) {
				return true;
			}
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return false;
	}
	
	public boolean updateQuestionDetailAvailable(QuestionDetail questionDetail)
			throws QuestionException {
		MongoClient mongoClient=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("question");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("questionId", questionDetail.getQuestionId());		
			searchQuery.put("subjectId", questionDetail.getSubjectId());
			BasicDBObject updateQuery = new BasicDBObject();
			updateQuery.put("questionDesc", questionDetail.getQuestionDesc());
			BasicDBObject updateOperation = new BasicDBObject();
			updateOperation.put("$set", updateQuery);
			WriteResult result = table.update(searchQuery, updateOperation, true, false);

			
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return false;
	}

}
