package com.taifexdemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * 讀取classpath下的指定properties文件
 */
public class PropUtil extends Properties {
	public PropUtil(String path){
		InputStream is = this.getClass().getResourceAsStream(path);
		try {
			super.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
