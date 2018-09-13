package com.dankook.scam_detector_sample.scam_detector;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BlackList {
	private ArrayList<String[]> blacklist;
	
	
	public BlackList() {
		blacklist = readBlackList();
	}
	
	public ArrayList<String[]> getBlacklist() {
		return blacklist;
	}
	
	private ArrayList<String[]> readBlackList() {
		
		JSONParser parser = new JSONParser();
		ArrayList<String[]> blacklist = new ArrayList<String[]>();
		
        try {

            // 해당 JSON 파일을 읽어옵니다.
            Object email = parser.parse(new FileReader(System.getProperty("user.dir")+ File.separator+ "blacklist.json"));
//        	Object email = parser.parse(new FileReader("C:\\Users\\jc\\eclipse-workspace\\scam_detector_sample\\src\\main\\java\\blacklist.json"));

            // 이차원 JSONArray 이기 때문에 1차 JSONArray에 있는 2차 JSONArray Size를 측정해 줍니다.
            JSONArray email_array = (JSONArray) email;
            int size = email_array.size();
            
            for(int i = 0; i < size; i++) {
            	
            	JSONArray jsonTemp = (JSONArray) email_array.get(i);
            	String[] blackTemp = new String[6];
            	
            	for(int j = 0; j < jsonTemp.size(); j++) {
            		blackTemp[j] = (String) jsonTemp.get(j);
            	}
            	
            	blacklist.add(blackTemp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
		return blacklist;
	}
}
