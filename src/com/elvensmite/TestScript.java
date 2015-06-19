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

public class TestScript {
	int world;
	
	public TestScript(int input) {
		world = input;
	}
	
	public Map<String,String> grabPlayerData() {
		Map<String,String> output = new LinkedHashMap<String,String>();
		
		String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=ranking";
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
						getAllMatches(input,"[0-9]{3}\\|[0-9]{3}");
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
			System.out.println(m.group(1));
			matches.add(m.group(1));
		}
		System.out.println(matches);
		return matches;
	}
	
	public int getTotalVillagesInContinent(int continent) {
		int totalVillages = 0;
		int offset = 0;
		boolean moreOffset = true;
		String https_url = null;
		while(moreOffset) {
			URL url;
			HttpsURLConnection con = null;
			https_url = "https://en"+world+".tribalwars.net/guest.php?screen=ranking&mode=con_player&offset="+offset+"&con="+continent;
			try {
				url = new URL(https_url);
				con = (HttpsURLConnection)url.openConnection();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				
			}
			if (con != null) {
				try {
					boolean tmpBool = false;
					BufferedReader br = 
							new BufferedReader(
								new InputStreamReader(con.getInputStream()));
					String input;
					String id = null;
					boolean foundId = false;
					boolean findVillageCount = false;
					boolean findVillageCount1 = false;
					while((input = br.readLine()) != null) {
						if(input.matches("\\s*<a class=\"\" href=\"/guest\\.php\\?screen=info_player&amp;id=[0-9]*\">")) {
							foundId = true;
						}else if(foundId) {
							if(input.contains("<") || input.contains(">")) {

							}else {
								foundId = false;
								findVillageCount = true;
							}
						}else if(findVillageCount) {
							if(input.matches("\\s*<td class=\"lit-item\">.*</td>")) {
								// This should be the total points of the tribe
								findVillageCount = false;
								findVillageCount1 = true;
							}
						}else if(findVillageCount1) {
							if(input.matches("\\s*<td class=\"lit-item\">(\\d+\\.?)*\\d+</td>")) {
								String tmp = (input.split("<td class=\"lit-item\">")[1]).split("</td>")[0];
								totalVillages += Integer.parseInt(tmp);
								findVillageCount = false;
								findVillageCount1 = false;
							}
						}
						if(input.contains("down &gt;&gt;&gt")) {
							System.out.println(https_url);
							System.out.println(input);
							tmpBool = true;
						}

					}
					if(tmpBool) {
						offset += 25;
						System.out.println("offset: "+offset);
					}else {
						moreOffset = false;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		return totalVillages;
	}
	
	
	
	public static void main(String args[]) {
		TestScript ts = new TestScript(78);
		System.out.println(ts.getTotalVillagesInContinent(26));
		
//		Map ts1 = ts.grabPlayerData();
//		Map ts2 = ts.grabPlayerVillageCoords(ts1);
		
	}
}

