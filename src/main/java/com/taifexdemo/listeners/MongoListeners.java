package com.taifexdemo.listeners;

import com.taifexdemo.utils.MongoUtil;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Component
public class MongoListeners implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent args0){
        System.out.println("創建資料庫MongoDB連接");
        MongoUtil.connect();
    }

    @Override
    public void contextDestroyed(ServletContextEvent args0){
        System.out.println("關閉資料庫MongoDB連接");
        MongoUtil.close();
    }
}
