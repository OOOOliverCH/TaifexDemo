package com.taifexdemo.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface IDemoService {

    public Object[] getTodayData(String today) throws IOException, NoSuchAlgorithmException, KeyManagementException;

    public Object[] searchDealData(String startDate, String endDate, String currency);
}
