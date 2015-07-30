package com.elvensmite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class TestScript {
	int world;
	
	public TestScript(int input) {
		world = input;
	}
	
	public List<String> getPlayerVillages(String id) {
		List<String> output = new ArrayList<String>();
		String https_url = "https://en"+world+".tribalwars.net/map/village.txt";
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
				while((input = br.readLine()) != null) {
					if(id.equals(input.split(",")[4])) {
						output.add(input.split(",")[2]+"|"+input.split(",")[3]);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}
	
	public String getPlayerIdFromRank(String rank) {
		String output = null;
		String https_url = "https://en"+world+".tribalwars.net/map/player.txt";
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
				while((input = br.readLine()) != null) {
					if(rank.equals(input.split(",")[5])) {
						output = input.split(",")[0];
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}

	
	public static void main(String args[]) {
//		TestScript ts = new TestScript(78);
//		System.out.println(ts.getPlayerVillages("9378928"));
//		System.out.println(ts.getPlayerIdFromRank("1"));
		
		
	}
}
