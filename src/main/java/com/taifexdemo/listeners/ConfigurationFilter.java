package com.taifexdemo.listeners;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ConfigurationFilter{

    private final String[] excludes = new String[]{
            "/demo",
    };

    private List<String> exUrl = Arrays.asList(excludes);

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MyFilter()); //添加過濾器
        registration.addUrlPatterns("/*");  //設置過濾路勁，/*表示所有路徑
        registration.setName("MyFilter");   //設置優先級
        registration.setOrder(1);   //設置優先級
        return registration;
    }

    public class MyFilter implements Filter{

        @Override
        public void init(FilterConfig arg0) throws ServletException {
        }

        @Override
        public void destroy() {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            //解決後端跨域的問題
            ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
            if(exUrl.contains(req.getRequestURI())){
                chain.doFilter(request, response);
                return ;
            }
            chain.doFilter(request, response);
        }
    }
}
