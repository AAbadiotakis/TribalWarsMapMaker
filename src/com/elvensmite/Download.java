package com.elvensmite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Download {
	
	public Download(String world, String url) {
		File theDir = new File(world);
		if(!theDir.exists()) {
			theDir.mkdir();
		}
		downloadFile("https://"+world+"."+url+"/map/player.txt", "player.txt");
		downloadFile("https://"+world+"."+url+"/map/village.txt", "village.txt");
		downloadFile("https://"+world+"."+url+"/map/ally.txt", "ally.txt");
		downloadFile("https://"+world+"."+url+"/map/conquer.txt","conquer.txt");
		downloadFile("https://"+world+"."+url+"/map/kill_att.txt","kill_att.txt");
		downloadFile("https://"+world+"."+url+"/map/kill_all.txt","kill_all.txt");
	}
	
	
	public static void downloadFile(String https_url, String fileName) {
		URL url;
		HttpsURLConnection con = null;
		try {
			url = new URL(https_url);
			con = (HttpsURLConnection)url.openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
		}
		if(con != null) {
			try {
				BufferedReader br = 
						new BufferedReader(
							new InputStreamReader(con.getInputStream()));
				String input;
				BufferedWriter writer = 
						new BufferedWriter(
								new OutputStreamWriter(
										new FileOutputStream(fileName), "utf-8"));
				while((input = br.readLine()) != null) {
					writer.append(input);
					writer.newLine();
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
