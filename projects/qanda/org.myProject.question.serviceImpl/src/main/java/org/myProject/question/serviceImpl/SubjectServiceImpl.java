package org.myProject.question.serviceImpl;
import java.net.UnknownHostException;

import org.myProject.common.logger.CommonLogger;
import org.myProject.service.question.QuestionException;
import org.myProject.service.question.SubjectDetail;
import org.myProject.service.question.SubjectService;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;


public class SubjectServiceImpl implements SubjectService{

	public void saveSubjectDetails(SubjectDetail subjectDetail)
			throws QuestionException {
		MongoClient mongoClient=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("subject");
			Gson gson=new Gson();
			String questionJson=gson.toJson(subjectDetail);
			DBObject document = (DBObject)JSON.parse(questionJson);
			table.insert(document);
			
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		
	}

	public String getSubjectDetail(SubjectDetail subjectDetail)
			throws QuestionException {
		MongoClient mongoClient=null;
		String subjectDetailJson=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("subject");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("subjectId", subjectDetail.getSubjectId());
			DBCursor cursor = table.find(searchQuery);

			while (cursor.hasNext()) {
				subjectDetailJson=cursor.next().toString();
			}
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return subjectDetailJson;

	}

	public String getAllSubject() throws QuestionException {
		MongoClient mongoClient=null;
		String subjectDetailJson=null;
		StringBuilder sb=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("subject");

			BasicDBObject searchQuery = new BasicDBObject();
			DBCursor cursor = table.find(searchQuery);
			sb=new StringBuilder(200);
			sb.append("[");
			while (cursor.hasNext()) {
				sb.append(cursor.next().toString()).append(",");
			}
			sb=new StringBuilder(sb.substring(0, sb.length()-1));
			sb.append("]");
			subjectDetailJson=sb.toString();
			
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return subjectDetailJson;
	}

}
