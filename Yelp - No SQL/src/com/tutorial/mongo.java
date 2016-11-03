package com.tutorial;
import com.mongodb.client.model.Aggregates.*;
import static java.util.Arrays.asList;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class mongo
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		MongoClient client = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		MongoCollection<Document> db = client.getDatabase("YelpApplication").getCollection("YelpReview");
//		FindIterable<Document> result = db.find(new Document("stars", new Document("$eq", 3.5)));
//		result.forEach(new Block<Document>() {
//
//			@Override
//			public void apply(final Document document) {
//				// TODO Auto-generated method stub	
//				System.out.println(document.toJson().trim());
//			}
//		});
		
//		AggregateIterable<Document> list = db.aggregate(asList(new Document("$lookup", new Document("from", "YelpBusiness").append("localField", "business_id").append("foreignField", "business_id").append("as","YelpBusinessReviews"))));
//		
//		list.forEach(new Block<Document> (){
//
//			@Override
//			public void apply(final Document document) {
//				// TODO Auto-generated method stub
//				System.out.println(document.toJson().trim());
//			}
//			
//		});
		ArrayList<String> str = new ArrayList<String>();
		str.add("Restaurants");
		
		ArrayList<String> str2 = new ArrayList<String>();
		str2.add("Alcohol_full_bar");
//		Document categorySelectQuery = new Document("$in",str);
//		MongoCollection<Document> b_collection=client.getDatabase("YelpApplication").getCollection("YelpBusinessAttributes");
//		 
//		FindIterable<Document> result = b_collection.find(new Document("categories",new Document("$all", str))).projection(fields(include("attribute"), excludeId()));
//		FindIterable<Document> result = b_collection.find(new Document("categories",new Document("$in", str)).append("attribute_keys", new Document("$in", str2))).projection(fields(include("name", "city", "stars", "state"), excludeId()));
//		MongoCollection<Document> b_collection1 =client.getDatabase("YelpApplication").getCollection("YelpBusiness");
//    	AggregateIterable<Document> list1 = b_collection1.aggregate(asList(match(eq("name", "Serenity Tattoo Removal")), 
//    																lookup("YelpReview", "business_id", "business_id", "YelpBusinessReviews"), 
//    																unwind("$YelpBusinessReviews"),
//    																lookup("YelpUser", "YelpBusinessReviews.user_id", "user_id", "UserDetails"),
//    																project(fields(computed("date", "$YelpBusinessReviews.date"),computed("business_id", "$business_id"), computed("stars", "$YelpBusinessReviews.stars"), computed("text", "$YelpBusinessReviews.text"), computed("userName", "$UserDetails.name"), computed("usefulVotes", "$YelpBusinessReviews.votes.useful"),  excludeId())),
//    																unwind("$userName")));
//    	try {
//			for(Document str1:list1){
//				System.out.println("list is: "+str1.toJson().toString().trim());
//   			   JSONParser parser = new JSONParser();
//               JSONObject jsonObject = (JSONObject) parser.parse(str1.toJson().toString().trim());
//               String date = (String) jsonObject.get("date");
//               System.out.println(date);
//               String stars = (String) jsonObject.get("stars").toString();
//               System.out.println(stars);
//               String text = (String) jsonObject.get("text");
//               String id = (String) jsonObject.get("userName");
//               String votesUseful=(String) jsonObject.get("usefulVotes").toString();
//               System.out.println(date + " " + stars + " " + text + " " + id + " " + votesUseful);
//			}
//		}
//   		catch(Exception e){
//   			System.out.println("Cant execute " + e);
//   			}
    	DB db1 = new MongoClient("localhost",27017).getDB("YelpApplication");
    	DBCollection dbcollection=db1.getCollection("YelpBusinessAttributes");
    	BasicDBObject q = new BasicDBObject();
    	BasicDBObject proj1 = new BasicDBObject();
    	q.put("categories", new BasicDBObject("$in", str));
    	q.append("attribute_keys", new BasicDBObject("$in", str2));
    	proj1.put("name", 1);
    	proj1.append("city", 1);
    	proj1.append("stars", 1);
    	proj1.append("state", 1);
    	proj1.append("_id", 0);
   	
    	String pointOfInterested_Value = "1610 Lake Las Vegas Pkwy\nHenderson, NV 89011";
        	
    	BasicDBObject full = new BasicDBObject();
        full.append("full_address", pointOfInterested_Value);
        BasicDBObject proj=new BasicDBObject();
        proj.put("Loc", 1);
        DBCursor cursor = dbcollection.find(full,proj);
        DBObject res;
        double latitude = 0;
        double longitude = 0;
        while(cursor.hasNext()){
        	res=cursor.next();
        	System.out.println("cursor is:"+res);
        	DBObject report=(DBObject) res.get("Loc");
        	latitude=Double.parseDouble(report.get("latitude").toString().trim());
        	longitude=Double.parseDouble(report.get("longitude").toString().trim());
        	System.out.println("latitude" + latitude);
        	System.out.println("longitude" + longitude);
        }

        	BasicDBList geoCoord = new BasicDBList();
            geoCoord.add(longitude);
            geoCoord.add(latitude);

            BasicDBList geoParams = new BasicDBList();
            geoParams.add(geoCoord);
            geoParams.add( 1/3963.2);

            q.append("Loc", new BasicDBObject("$geoWithin", new BasicDBObject("$centerSphere", geoParams)));
            System.out.println("FromHere");
            System.out.println(q);
            DBCursor sex = dbcollection.find(q,proj1);
    	       while(sex.hasNext()){
    	       	
    	       	System.out.println(sex.next().toString());
    	       }
//            }
//		System.out.println(result);
//		List<String> attributes = new ArrayList<String>();
//		list1.forEach(new Block<Document> (){
//        	@Override
//			public void apply(final Document document) {
//				// TODO Auto-generated method stub
//        		attributes.add(document.toJson().toString().trim());
//			}
//        	});
//        List<String> attributeList = new ArrayList<String>(new LinkedHashSet<String>(attributes));
//        for(String str1:attributeList){
//        	System.out.println(str1);
//        }
	}
}