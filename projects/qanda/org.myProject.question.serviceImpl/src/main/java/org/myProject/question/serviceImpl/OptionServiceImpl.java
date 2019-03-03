package org.myProject.question.serviceImpl;

import java.net.UnknownHostException;

import org.myProject.common.logger.CommonLogger;
import org.myProject.service.question.OptionDetail;
import org.myProject.service.question.OptionService;


import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class OptionServiceImpl implements OptionService{

	public void saveOptionDetails(OptionDetail optionDetail) {
			MongoClient mongoClient=null;
			try {
				mongoClient = new MongoClient( "localhost" , 27017 );
				DB db = mongoClient.getDB("questionDb");
				DBCollection table = db.getCollection("option");
				Gson gson=new Gson();
				String optionJson=gson.toJson(optionDetail);
				DBObject document = (DBObject)JSON.parse(optionJson);
				table.insert(document);
				
			} catch (UnknownHostException e) {
				CommonLogger.errorTrace(e);
			}finally{
				mongoClient.close();
			}

		}

	public String getOptionDetail(OptionDetail optionDetail) {
		MongoClient mongoClient=null;
		String optionDetailJson=null;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB("questionDb");
			DBCollection table = db.getCollection("option");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("questionId", optionDetail.getQuestionId());		
			searchQuery.put("subjectId", optionDetail.getSubjectId());
			searchQuery.put("optionId", optionDetail.getOptionId());
			DBCursor cursor = table.find(searchQuery);

			while (cursor.hasNext()) {
				optionDetailJson=cursor.next().toString();
			}
		} catch (UnknownHostException e) {
			CommonLogger.errorTrace(e);
		}finally{
			mongoClient.close();
		}
		return optionDetailJson;
	}
	
	

		
	}


