package com.taifexdemo.utils;

import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//連接MongoDB資料庫相關工具類
public class MongoUtil {
	private static MongoDatabase db;
	private static MongoClient mg;

	//連接MongoDB
	public static void connect(){

		PropUtil pu = new PropUtil("/mongo.properties");
		String url = pu.getProperty("db_url");


		MongoClientURI connStr = new MongoClientURI(url);
		mg = new MongoClient(connStr);

		//取得url最後部分的資料庫名
		String dbName = url.substring(url.lastIndexOf("/")+1);
		db = mg.getDatabase(dbName);
	}

	//關閉MongoDB連接
	public static void close(){
		if(mg != null){

			mg.close();
			mg = null;
		}
	}

	//根據ID進行刪除
	public static void del(String tbl, String id){
		Document obj = new Document("_id", new ObjectId(id));
		delByObj(tbl, obj);
	}

	//根據條件進行刪除
	public static void delByObj(String tbl, Document obj){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.deleteMany(obj);
	}

	//保存對象
	public static void save(String tbl, Document obj){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.insertOne(obj);
	}

	//保存Map對象
	public static void save(String tbl, Map map){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.insertOne(new Document(map));
	}

	//保存JSON對象
	public static void save(String tbl, Object obj){
		MongoCollection<Document> coll = db.getCollection(tbl);
		Document target = Document.parse(JSON.toJSONString(obj));
		target.remove("id");
		coll.insertOne(target);
	}

	//保存JSON對象，返回ID
	public static String saveWithId(String tbl, Object obj){
		return saveWithId(tbl, obj, null);
	}

	//保存JSON對象，返回ID
	public static String saveWithId(String tbl, Object obj, String[] excludes){
		MongoCollection<Document> coll = db.getCollection(tbl);
		Document target = Document.parse(JSON.toJSONString(obj));
		target.remove("id");
		ObjectId id = new ObjectId();
		target.put("_id", id);
		coll.insertOne(target);

		return id.toString();
	}

	//保存多個JSON對象
	public static void save(String tbl, List objs){
		MongoCollection<Document> coll = db.getCollection(tbl);

		List<Document> docs = new ArrayList<Document>(objs.size());
		for(Object obj : objs){
			Document doc = Document.parse(JSON.toJSONString(obj));
			doc.remove("id");
			docs.add(doc);
		}

		coll.insertMany(docs);
	}

	//根據ID更新整個JSON對象
	public static void update(String tbl, String id, Object obj){
		MongoCollection<Document> coll = db.getCollection(tbl);

		Document target = Document.parse(JSON.toJSONString(obj));
		target.remove("id");
		Document idObj = new Document("_id", new ObjectId(id));

		coll.replaceOne(idObj, target);
	}

	//根據條件更新部分屬性值
	public static void update(String tbl, Document obj1, Document obj2){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.updateMany(obj1, new Document("$set", obj2));
	}

	//遞增1
	public static void inc(String tbl, Document obj1, Document obj2){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.updateMany(obj1, new Document("$inc", obj2));
	}

	//更具條件增加值到屬性中
	public static void addToSet(String tbl, Document obj1, Document obj2){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.updateMany(obj1, new Document("$addToSet", obj2));
	}

	//根據條件刪除集合中的值
	public static void pull(String tbl, Document obj1, Document obj2){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.updateMany(obj1, new Document("$pull", obj2));
	}

	//根據條件刪除屬性值
	public static void unset(String tbl, Document obj1, Document obj2){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.updateMany(obj1, new Document("$unset", obj2));
	}

	//根據條件與排序查詢，取得查詢後的第一條數據
	public static Document first(String tbl, Document query, Document fields,Document sort){

		MongoCollection<Document> coll = db.getCollection(tbl);

		FindIterable<Document> docs = null;
		if(query == null)	docs = coll.find();
		else				docs = coll.find(query);

		if(fields != null)	docs = docs.projection(fields);
		if(sort != null)	docs.sort(sort);
		return docs.first();
	}

	//根據ID取得數據
	public static Document get(String tbl, String id){
		MongoCollection<Document> coll = db.getCollection(tbl);
		Document obj = new Document("_id", new ObjectId(id));
		return coll.find(obj).first();
	}

	//根據條件進行查詢
	public static List<Document> search(String tbl, Document query, Document fields){
		return search(tbl, query, fields, null);
	}

	//根據條件與排序，進行查詢
	public static List<Document> search(String tbl,Document query, Document fields, Document sort){

		MongoCollection<Document> coll = db.getCollection(tbl);

		FindIterable<Document> docs = null;
		if(query == null)	docs = coll.find();
		else				docs = coll.find(query);

		if(fields != null)	docs = docs.projection(fields);
		if(sort != null)	docs.sort(sort);

		return getList(docs);
	}

	//根據條件與排序、限制條數，進行查詢
	public static List<Document> search(String tbl,Document query, Document fields, Document sort,Integer limit){

		MongoCollection<Document> coll = db.getCollection(tbl);

		FindIterable<Document> docs = null;
		if(query == null)	docs = coll.find();
		else				docs = coll.find(query);

		if(fields != null)	docs = docs.projection(fields);
		if(sort != null)	docs.sort(sort);

		docs.limit(limit);

		return getList(docs);
	}

	//創建索引
	public static void createIndex(String tbl,Document indexValue){
		MongoCollection<Document> coll = db.getCollection(tbl);
		coll.createIndex(indexValue);
	}

	//取得整張表
	public static List<Document> list(String tbl){
		MongoCollection<Document> coll = db.getCollection(tbl);
		FindIterable<Document> docs = coll.find();
		return getList(docs);
	}

	//按條件取得表
	public static List<Document> list(String tbl, int start, int pageSize){
		MongoCollection<Document> coll = db.getCollection(tbl);
		FindIterable<Document> docs = coll.find().skip(start).limit(pageSize);
		return getList(docs);
	}

	//分組
	public static List<Document> group(String tbl, Document query, Document group){

		List<Document> groups = Arrays.asList(new Document("$match", query),
				new Document("$group", group));

		MongoCollection<Document> coll = db.getCollection(tbl);
		AggregateIterable<Document> docs = coll.aggregate(groups).allowDiskUse(true);

		List<Document> result = new ArrayList<Document>();
		for(Document doc : docs)	result.add(doc);
		return result;
	}

	//計算表的數據數量
	public static int count(String tbl){
		MongoCollection<Document> coll = db.getCollection(tbl);
		return ((Long)coll.count()).intValue();
	}

	//根據條件計算表的數據數量
	public static int count(String tbl, Document query){
		MongoCollection<Document> coll = db.getCollection(tbl);
		return ((Long)coll.count(query)).intValue();
	}

	//查詢結果轉換成List集合
	private static List<Document> getList(FindIterable<Document> docs){
		List<Document> result = new ArrayList<Document>();
		for(Document doc : docs)	result.add(doc);
		return result;
	}

}
