package com.taifexdemo.controller;

import com.taifexdemo.service.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @Autowired
    private IDemoService iDemoService;

    @RequestMapping(value = "/get_today_data", method = RequestMethod.GET)
    public Object[] getTodayData(String today) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        return iDemoService.getTodayData(today);
    }

    @RequestMapping(value = "/search_deal_data", method = RequestMethod.POST)
    public Object[] searchDealData(String startDate, String endDate, String currency){
        return iDemoService.searchDealData(startDate, endDate, currency);
    }
}
