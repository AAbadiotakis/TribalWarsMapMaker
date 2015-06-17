package com.elvensmite;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;

public class ParseODT {
	int world;
	
	public ParseODT(int input) {
		world = input;
	}
	
	public Map<String,String> grabODTData() {
		Map<String,String> output = new LinkedHashMap<String,String>();
		String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=ranking&mode=kill_player&type=all";
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
				String id = null;
				boolean foundId = false;;
				while((input = br.readLine()) != null) {
					if(input.matches("\\s*<a class=\"\" href=\"/guest\\.php\\?screen=info_player&amp;id=[0-9]*\">")) {
						id = (input.split("id=")[1]).split("\">")[0];
						foundId = true;
					}else if(foundId) {
						if(input.contains("<") || input.contains(">")) {
							
						}else {
							foundId = false;
							output.put(input.trim(), id);
						}
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return output;
	}
	
	
	public Map<String,List<String>> grabPlayerVillageCoords(Map<String,String> m) {
		Map<String,List<String>> output = new LinkedHashMap<String,List<String>>();
		for (String key : m.keySet()) {
			String id = m.get(key);
			List<String> l = new ArrayList<String>();
			String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=info_player&ajax=fetch_villages&player_id="+id;
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
						l.addAll(getAllMatches(input,"[0-9]{3}\\|[0-9]{3}"));
					}
					output.put(key, l);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			
			String https_url2 = "https://en"+world+".tribalwars.net/guest.php?screen=info_player&id="+id;
			URL url2;
			HttpsURLConnection con2 = null;
			try {
				url2 = new URL(https_url2);
				con2 = (HttpsURLConnection)url2.openConnection();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(con2 != null) {
				try {
					BufferedReader br = 
							new BufferedReader(
								new InputStreamReader(con2.getInputStream()));
					String input;
					while((input = br.readLine()) != null) {
						if(input.matches("\\s*<td>[0-9]{3}\\|[0-9]{3}</td>")) {
							l.add((input.split("<td>")[1]).split("</td>")[0]);
						}
					}
					output.put(key, l);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}		
		return output;
	}
	
	public static List<String> getAllMatches(String text, String regex) {
		List<String> matches = new ArrayList<String>();
		Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
		while(m.find()) {
			matches.add(m.group(1));
		}
		return matches;
	}
}
