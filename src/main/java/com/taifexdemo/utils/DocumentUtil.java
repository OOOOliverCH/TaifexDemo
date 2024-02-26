package com.taifexdemo.utils;

import com.alibaba.fastjson.JSON;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

//實體類格式轉換工具類
public class DocumentUtil {

    //把mongodb讀出的document轉成Class類的實例
    public static Object convert(Class c, Document doc){
        doc = handleId(doc);
        Object obj = JSON.toJSON(doc);
        return JSON.parseObject(obj.toString(), c);
    }

    //轉换List<Document>為List<Class>
    public static List convertList(Class c, List<Document> docs){
        for(Document doc : docs)	doc = handleId(doc);
        Object obj = JSON.toJSON(docs);
        return Arrays.asList(JSON.parseArray(obj.toString(),c).toArray());
    }

    //轉换List<Document>為List<Class>
    public static List convertListExId(Class c, List<Document> docs){
        Object obj = JSON.toJSON(docs);
        return Arrays.asList(JSON.parseArray(obj.toString(),c).toArray());
    }

    //處理_id ObjectId為id String
    private static Document handleId(Document doc){
        if (doc.containsKey("_id")) {
            doc.put("id", doc.getObjectId("_id").toString());
            doc.remove("_id");
        }
        return doc;
    }
}
