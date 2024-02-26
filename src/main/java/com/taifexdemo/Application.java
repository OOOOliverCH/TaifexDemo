package com.taifexdemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@Configuration
public class Application {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
        System.out.println("啟動成功");
    }
}
