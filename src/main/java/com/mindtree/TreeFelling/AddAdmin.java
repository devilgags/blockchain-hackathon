/**
 * 
 */
package com.mindtree.TreeFelling;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class AddAdmin {

	public static void main(String[] args) {
		MongoClient mongo = new MongoClient( "smartretail.westus2.cloudapp.azure.com" , 51010 );
		DB db = mongo.getDB("smartRetaildb");
		DBCollection users	 = db.getCollection("users");
		BasicDBObject document = new BasicDBObject();
		document.put("userid", "admin@gmail.com");
		document.put("userName", "admin");
		document.put("password", "Welcome123");
		document.put("type", "SHOP_ADMIN");
		document.put("phoneNo", "9876543210");
		users.insert(document);
		
		mongo.close();
	}
}
