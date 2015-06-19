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

public class ParseContinentPlayers {
	int world;
	
	public ParseContinentPlayers(int input) {
		world = input;
	}
	
	public Map<String,List<String>> grabContinentPlayerData() {
		Map<String,List<String>> output = new LinkedHashMap<String,List<String>>();
		for(int continent=0;continent<99;continent++) {
				int totalVillages = getTotalVillagesInContinent(continent);
				String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=ranking&mode=con_player&con="+continent;
				URL url;
				HttpsURLConnection con = null;
				try {
					url = new URL(https_url);
					con = (HttpsURLConnection)url.openConnection();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					
				}
				
				if (con != null) {
					try {
						BufferedReader br = 
								new BufferedReader(
									new InputStreamReader(con.getInputStream()));
						String input;
						String id = null;
						List<String> l = new ArrayList<String>();
						boolean foundId = false;
						boolean findVillageCount = false;
						boolean firstRanking = true;
						boolean secondRanking = false;
						while((input = br.readLine()) != null) {
							if(firstRanking) {
								if(input.matches("\\s*<a class=\"\" href=\"/guest\\.php\\?screen=info_player&amp;id=[0-9]*\">")) {
									id = (input.split("id=")[1]).split("\">")[0];
									l.add(id);
									foundId = true;
								}else if(foundId) {
									if(input.contains("<") || input.contains(">")) {

									}else {
										foundId = false;
										findVillageCount = true;
										l.add(input.trim());
									}
								}else if(findVillageCount) {
									if(input.matches("\\s*<td class=\"lit-item\">[0-9]*</td>")) {
										String tmp = (input.split("<td class=\"lit-item\">")[1]).split("</td>")[0];
										float percentDom = (float) Float.parseFloat(tmp)/totalVillages;
										l.add(""+percentDom);
										output.put(""+continent, l);
										firstRanking = false;
										secondRanking = true;
										findVillageCount = false;
									}
								}
							}else if(secondRanking) {
								if(input.matches("\\s*<a class=\"\" href=\"/guest\\.php\\?screen=info_player&amp;id=[0-9]*\">")) {
									id = (input.split("id=")[1]).split("\">")[0];
									l.add(id);
									foundId = true;
								}else if(foundId) {
									if(input.contains("<") || input.contains(">")) {

									}else {
										foundId = false;
										findVillageCount = true;
										l.add(input.trim());
									}
								}else if(findVillageCount) {
									if(input.matches("\\s*<td class=\"lit-item\">[0-9]*</td>")) {
										String tmp = (input.split("<td class=\"lit-item\">")[1]).split("</td>")[0];
										float percentDom = (float) Float.parseFloat(tmp)/totalVillages;
										l.add(""+percentDom);
										output.put(""+continent, l);
										firstRanking = false;
										secondRanking = false;
										findVillageCount = false;
									}
								}
							}
							
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
		return output;
	}
	
	public Map<String,String> getTopFifteenPlayerDom(Map<String,List<String>> m) {
		Map<String,String> output = new LinkedHashMap<String,String>();
		List<Float> topFifteen = new ArrayList<Float>();
		for(int i=0;i<15;i++) {
			Float highestVal = (float) 0.00;
			for(String key: m.keySet()) {
				List<String> l = m.get(key);
				Float currentVal = Float.parseFloat(l.get(2));
				if(currentVal > highestVal && !topFifteen.contains(currentVal)) {
					highestVal = currentVal;
				}
			}
			topFifteen.add(highestVal);
		}
		
		for(String key: m.keySet()) {
			List<String> l = m.get(key);
			for(Float i: topFifteen) {
				if(l.contains(Float.toString(i))) {
					output.put(l.get(1), l.get(0));
				}
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
							tmpBool = true;
						}

					}
					if(tmpBool) {
						offset += 25;
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
		ParseContinentPlayers pcp = new ParseContinentPlayers(78);
//		Map<String, List<String>> pcp1 = pcp.grabContinentPlayerData();
//		Map<String,String> pcp2 = pcp.getTopFifteenPlayerDom(pcp1);
//		Map<String,List<String>> pcp3 = pcp.grabPlayerVillageCoords(pcp2);
//		System.out.println(pcp3);
		System.out.println(""+pcp.getTotalVillagesInContinent(26));
	}
}
