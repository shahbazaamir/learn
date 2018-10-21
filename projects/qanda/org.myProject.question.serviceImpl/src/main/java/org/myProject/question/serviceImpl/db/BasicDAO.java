package org.myProject.question.serviceImpl.db;

import java.net.UnknownHostException;




import org.myProject.question.serviceImpl.Logger;
import org.myProject.question.serviceImpl.config.Configuration;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class BasicDAO {
	public void saveDetails(Object data,String collection) {
	MongoClient mongoClient=null;
	try {
		mongoClient = new MongoClient( Configuration.HOST , Configuration.PORT );
		DB db = mongoClient.getDB(Configuration.DB);
		DBCollection table = db.getCollection(collection);
		Gson gson=new Gson();
		String dataJson=gson.toJson(data);
		DBObject document = (DBObject)JSON.parse(dataJson);
		table.insert(document);
		
	} catch (UnknownHostException e) {
		Logger.printStackTrace(e);
	}finally{
		mongoClient.close();
	}
	}
}
