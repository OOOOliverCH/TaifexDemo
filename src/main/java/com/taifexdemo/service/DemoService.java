package com.taifexdemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taifexdemo.entity.ReturnMessage;
import com.taifexdemo.entity.Currency;
import com.taifexdemo.utils.DateUtil;
import com.taifexdemo.utils.DocumentUtil;
import com.taifexdemo.utils.MongoUtil;
import org.bson.Document;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DemoService implements IDemoService{

    private final String API_URL = "https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates";
    private final String DB_CURRENCY = "currency";

    //題目一，每日18:00自動獲取當天匯率比，並且將日期、匯率、幣種存入資料庫中
    @Override
    public Object[] getTodayData(String today){
        String result = getUrlReturnDate();
        JSONArray array = JSON.parseArray(result);
        Currency udl = new Currency();
        for (int i = 0; i < array.size(); i++) {
            if(today.equals(array.getJSONObject(i).getString("Date"))){
                System.out.println("匹配到日期"+today+"，美元/台幣匯率比為"+array.getJSONObject(i).get("USD/NTD"));
                udl.setDate(array.getJSONObject(i).getString("Date"));
                udl.setUsd_ntd(array.getJSONObject(i).getString("USD/NTD"));
                udl.setCurrency("usd");
                MongoUtil.save(DB_CURRENCY, udl);

                return new Object[]{new ReturnMessage("0000", "數據已存入資料庫中"), udl};
            }
        }

        return new Object[]{new ReturnMessage("E001", "未查詢到當天成交數據")};
    }

    //發送請求到API URL獲取當天成交數據
    private String getUrlReturnDate(){
        String result = "";
        URL realUrl = null;
        try {
            realUrl = new URL(API_URL);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            // 打开和URL之间的连接
            HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("URL格式格式錯誤或無效，請確認URL是否正確");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("算法異常，當前環境不支持或不可用");
        } catch (KeyManagementException e) {
            e.printStackTrace();
            System.out.println("密鑰管理異常，請確認操作是否正確");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("操作異常，請確認輸入輸出是否正確");
        }
        return  result;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    //題目二，根據查詢條件查詢相關數據
    @Override
    public Object[] searchDealData(String startDate, String endDate, String currency){
        if(DateUtil.compareDate(startDate, DateUtil.getLastYear(), "yyyyMMdd") == -1 ||
            DateUtil.compareDate(endDate, DateUtil.getLastDay(), "yyyyMMdd") == 1){
            return new Object[]{new ReturnMessage("E001", "日期區間不符")};
        }
        Document query = new Document("date", new Document("$gte", startDate).append("$lte", endDate)).append("currency", currency);
        List<Document> doc = MongoUtil.search(DB_CURRENCY, query, null);
        List<Currency> udl = new ArrayList<Currency>();
        if(doc.size()>0){
            udl = DocumentUtil.convertList(Currency.class, doc);
            return new Object[]{new ReturnMessage("0000", "查詢到"+udl.size()+"條數據"), udl};
        }else{
            return new Object[]{new ReturnMessage("0000", "未查詢到數據"), udl};
        }
    }
}
