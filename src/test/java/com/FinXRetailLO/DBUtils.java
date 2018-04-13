package com.FinXRetailLO;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jxl.read.biff.BiffException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.tavant.kwutils.KWVariables;

public class DBUtils {

	public static MongoClient connectToDB(String args) throws BiffException,
			InvalidFormatException, IOException {

		System.out.println("Connecting to DB");
		// String data = KWVariables.getVariables().get(args);
		MongoClient mongo = new MongoClient(args, 27017);
		System.out.println("Connected");
		return mongo;

	}

	public static void createDocumentInCollection(String database,
			String collection) throws BiffException, InvalidFormatException,
			IOException {
		MongoClient mongo = connectToDB("10.210.76.74");
		DB db = mongo.getDB(database);
		DBCollection table = db.getCollection(collection);
		BasicDBObject document = new BasicDBObject();
		document.put("leadId", "1000");
		document.put("firstName", "Automated");
		document.put("lastName", "Script");
		table.insert(document);
	}

	public static void updateDocumentInCollection(String database,
			String collection) throws BiffException, InvalidFormatException,
			IOException {
		MongoClient mongo = connectToDB("10.210.76.74");
		DB db = mongo.getDB(database);
		DBCollection table = db.getCollection(collection);
		BasicDBObject query = new BasicDBObject();
	}

	/**
	 * Sorts all collections in descending order and return top most result
	 * 
	 * 
	 * @param database
	 * @param collection
	 * @param columnName
	 * @return
	 * @throws BiffException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Map sortDocsInDescNreturnTopMostResult(String database,
			String collection, String columnName) throws BiffException,
			InvalidFormatException, IOException {
		MongoClient mongo = connectToDB("10.210.76.74");
		DB db = mongo.getDB(database);
		DBCollection table = db.getCollection(collection);
		DBCursor cursor = table.find().sort(new BasicDBObject(columnName, -1));
		return cursor.next().toMap();
	}

	/**
	 * Sorts all collections in ascending order and return top most result
	 * 
	 * @param database
	 * @param collection
	 * @param columnName
	 * @return
	 * @throws BiffException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Map sortDocsInAscNreturnTopMostResult(String database,
			String collection, String columnName) throws BiffException,
			InvalidFormatException, IOException {
		MongoClient mongo = connectToDB("10.210.76.74");
		DB db = mongo.getDB(database);
		DBCollection table = db.getCollection(collection);
		DBCursor cursor = table.find().sort(new BasicDBObject(columnName, 1));
		return cursor.next().toMap();
	}
	/**
	 * @param args
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws BiffException
	 */
	public static void main(String[] args) throws BiffException,
			InvalidFormatException, IOException {
		

	}

}
