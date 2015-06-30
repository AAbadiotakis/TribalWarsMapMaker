package com.elvensmite.MapCreators;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import com.elvensmite.Download;
import com.elvensmite.StartScript;

public class TribeDominance {
	int world;
	int[] ColorMap;
	
	public TribeDominance(int input) {
		world = input;
		ColorMap = StartScript.ColorMap;
	}
	
	public void createMap() {
		Map<String, List<String>> continentTribeData = grabContinentTribeData();
		System.out.println("continentTribeData");
		System.out.println(continentTribeData);
		Map<String, String> TopFifteen = getTopFifteenTribeDom(continentTribeData);
		System.out.println("TopFifteen");
		System.out.println(TopFifteen);
//		Map<String, List<String>> tribeVillageData = grabTribePlayers(TopFifteen);
		
		int lowestX = 1000;
		int lowestY = 1000;
		int highestX = 0;
		int highestY = 0;
		
		for(String key: continentTribeData.keySet()) {
			List<String> list = continentTribeData.get(key);
			String tribeId = list.get(0);
			List<String> villageCoords = getTribeVillages(tribeId);
			System.out.println(villageCoords);
			for(String villageCoord: villageCoords) {
				int xCoord = Integer.parseInt((villageCoord.split("\\|")[0]));
				int yCoord = Integer.parseInt((villageCoord.split("\\|")[1]));
				int xTmp = xCoord;
				int yTmp = yCoord;
				while(xTmp > 9)
					xTmp = xTmp/10;
				while(yTmp > 9)
					yTmp = yTmp/10;
				if(lowestX > xTmp)
					lowestX = xTmp;
				if(lowestY > yTmp)
					lowestY = yTmp;
				if(highestX < xTmp)
					highestX = xTmp;
				if(highestY < yTmp)
					highestY = yTmp;
			}
		}
		
		lowestX = lowestX*100;
		lowestY = lowestY*100;
		System.out.println("lowestX: "+lowestX);
		System.out.println("lowestY: "+lowestY);
		
		highestX = (highestX*100) + 100;
		highestY = (highestY*100) + 100;
		System.out.println("highestX: "+highestX);
		System.out.println("highestY: "+highestY);
		
		int width = highestX - lowestX;
		int height = highestY - lowestY;
		
		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(72,71,82));
		graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
		int ranking = 0;
		int drawY = 50;
		
		for(String key: TopFifteen.keySet()) {
			if(ranking < ColorMap.length) {
				List<String> l = getTribeVillages(TopFifteen.get(key));
				for(String str: l) {
					int xCoord = Integer.parseInt((str.split("\\|")[0]));
					int yCoord = Integer.parseInt((str.split("\\|")[1]));
					xCoord -= lowestX;
					yCoord -= lowestY;
					graphics.setColor(new Color(ColorMap[ranking]));
					graphics.fillRect(xCoord-2, yCoord-2, 4, 4);
					graphics.setColor(Color.BLACK);
					graphics.drawRect(xCoord-3, yCoord-3, 5, 5);
				}
				ranking++;
			}
		}
		
		//Print Continent numbers
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(x%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
				if(y%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
				if(x%100 == 0 && y%100 == 0 && x != highestX && y != highestY) {
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					FontMetrics fm = graphics.getFontMetrics();
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+(y+lowestY)/100+(x+lowestX)/100, x, y+fm.getHeight());
				}
			}
		}
		
		//Print tribe name per K
		//Print % Tribe Owned/total K
		for(String key: continentTribeData.keySet()) {
			String x = ""+key.charAt(1);
			String y = ""+key.charAt(0);
			
			int xCoord = Integer.parseInt(x);
			int yCoord = Integer.parseInt(y);
			
			xCoord = xCoord*100;
			yCoord = yCoord*100;
			
			xCoord -= lowestX;
			yCoord -= lowestY;
			
			if(continentTribeData.containsKey(key)) {
				List<String> l = continentTribeData.get(key);
				String tribe = l.get(1);
				String f = l.get(2);
				float f1 = Float.parseFloat(f);
				f1 = f1*100;
				f = String.format("%.2f", f1);
				f += "%";
				int fontSize = 12;
				graphics.setFont(new Font("Dialog",Font.BOLD, fontSize));
				FontMetrics fm = graphics.getFontMetrics();
				if(fm.stringWidth(tribe) > 200) {
					boolean stay = true;
					while(stay) {
						fontSize--;
						graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
						fm = graphics.getFontMetrics();
						if(fm.stringWidth(tribe) < 200)
							stay = false;
					}
				}
				graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
				graphics.setPaint(new Color(0,0,0));
				fm = graphics.getFontMetrics();
				int moveX = fm.stringWidth(tribe);
				graphics.drawString(tribe, xCoord+((100-moveX)/2), yCoord+50);
				graphics.setFont(new Font("Dialog",Font.BOLD,12));
				moveX = fm.stringWidth(f);
				graphics.drawString(f,xCoord+((100-moveX)/2),yCoord+75);	
			}
		}
		
		ranking = 0;
		graphics.dispose();
		BufferedImage fullImage = new BufferedImage(width+200,height+30,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullImage.createGraphics();
		g.drawImage(img, 0, 30, width, height+30, 0, 0, img.getWidth(), img.getHeight(), null);
		String header = "Tribe Dominance";
		g.setFont(new Font("TimesRoman",Font.BOLD,21));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(header, (width/2)-((fm.stringWidth(header))/2), fm.getHeight());
		g.setFont(new Font("Dialog", Font.BOLD, 14));
		fm = graphics.getFontMetrics();
		drawY = fm.getHeight();
		int fontSize = 20;
		boolean isTrue = true;
		while(isTrue) {
			g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
			fm = g.getFontMetrics();
			if(height - 15*fm.getHeight() > 20) {
				isTrue = false;
			}else {
				fontSize--;
			}
		}
		int resetFont = fontSize;
		//Print Tribe key
		for(String key: TopFifteen.keySet()) {
			if(ranking < ColorMap.length) {
				fontSize = resetFont;
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				fm = g.getFontMetrics();
				if(fm.stringWidth(key) > 200) {
					boolean stay = true;
					while(stay) {
						fontSize--;
						g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
						fm = g.getFontMetrics();
						if(fm.stringWidth(key) <= 200)
							stay = false;
					}
				}
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				g.setPaint(new Color(ColorMap[ranking]));
				g.drawString(key, width, 15+drawY);
				drawY+= fm.getHeight();
				ranking++;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy 'EST'");
		String d = sdf.format(new Date());
		g.setFont(new Font("TimesRoman",Font.BOLD,10));
		g.setColor(Color.WHITE);
		fm = g.getFontMetrics();
		g.drawString(d,width,(height+30)-fm.getHeight());
		g.dispose();
		Image scaledImage = fullImage.getScaledInstance(750, (int) (fullImage.getHeight()*(750.00/fullImage.getWidth())), Image.SCALE_SMOOTH);		
		BufferedImage buffered = new BufferedImage(750, (int) (fullImage.getHeight()*(750.00/fullImage.getWidth())), BufferedImage.TYPE_INT_RGB);
		Graphics2D bimg = buffered.createGraphics();
		bimg.drawImage(scaledImage, 0, 0, null);
		g.dispose();
		
		File f = new File("w"+world+File.separator+"TribeDominance.jpg");
		try {
			ImageIO.write(buffered, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	
	
	public Map<String,List<String>> grabContinentTribeData() {
		Map<String,List<String>> output = new LinkedHashMap<String,List<String>>();
		for(int continent=0;continent<99;continent++) {
				int totalVillages = getTotalVillagesInContinent(continent);
				String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=ranking&mode=con_ally&con="+continent;
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
						boolean findVillageCount1 = false;
						boolean firstRanking = true;
						boolean secondRanking = false;
						while((input = br.readLine()) != null) {
							if(firstRanking) {
								if(input.matches("\\s*<a href=\"/guest\\.php\\?screen=info_ally&amp;id=[0-9]*\">")) {
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
									if(input.matches("\\s*<td class=\"lit-item\">.*</td>")) {
										// This should be the total points of the tribe
										findVillageCount = false;
										findVillageCount1 = true;
									}
								}else if(findVillageCount1) {
									if(input.matches("\\s*<td class=\"lit-item\">(\\d+\\.?)*\\d+</td>")) {
										String tmp = (input.split("<td class=\"lit-item\">")[1]).split("</td>")[0];
										tmp = tmp.replaceAll("\\.", "");
										float percentDom = (float) Float.parseFloat(tmp)/totalVillages;
										l.add(""+percentDom);
										output.put(""+continent, l);
										firstRanking = false;
										secondRanking = true;
										findVillageCount = false;
										findVillageCount1 = false;
									}
								}
							}else if(secondRanking) {
								if(input.matches("\\s*<a href=\"/guest\\.php\\?screen=info_ally&amp;id=[0-9]*\">")) {
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
									if(input.matches("\\s*<td class=\"lit-item\">(\\d+\\.?)*\\d+</td>")) {
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
	
	public Map<String,String> getTopFifteenTribeDom(Map<String,List<String>> m) {
		Map<String,String> output = new LinkedHashMap<String,String>();
		List<Float> topFifteen = new ArrayList<Float>();
		List<String> topFifteenTribes = new ArrayList<String>();
		for(String key: m.keySet()) {
			List<String> l = m.get(key);
			Float currentVal = Float.parseFloat(l.get(2));
			topFifteen.add(currentVal);
		}
		Collections.sort(topFifteen, new Comparator<Float> () {
			@Override
			public int compare(Float arg0, Float arg1) {
				int result = Float.compare(arg1,arg0);
				return result;
			}
		});
		for(int i=0;i<15;i++) {
			for(String key: m.keySet()) {
				List<String> l = m.get(key);
				Float currentVal = Float.parseFloat(l.get(2));
				String currentTribe = l.get(1);
				if(currentVal.equals(topFifteen.get(i))) {
					if(!topFifteenTribes.contains(currentTribe)) {
						topFifteenTribes.add(currentTribe);
						output.put(currentTribe, l.get(0));
					}
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
	
	public Map<String,List<String>> grabTribePlayers(Map<String,String> m) {

		Map<String,List<String>> output = new LinkedHashMap<String,List<String>>();
		for (String key : m.keySet()) {
			String id = m.get(key);
			List<String> l = new ArrayList<String>();
			List<String> l2 = new ArrayList<String>();
			String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=info_member&id="+id;
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
						if(input.matches("\\s*<a href=\"/guest\\.php\\?screen=info_player&amp;id=[0-9]*\">.*</a>")) {
							l.add((input.split("id=")[1]).split("\">")[0]);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for(String playerId : l) {
				String https_url2 = "https://en"+world+".tribalwars.net/guest.php?screen=info_player&id="+playerId;
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
						BufferedReader br2 = 
								new BufferedReader(
									new InputStreamReader(con2.getInputStream()));
						String input;
						while((input = br2.readLine()) != null) {
							if(input.matches("\\s*<td>[0-9]{3}\\|[0-9]{3}</td>")) {
								l2.add((input.split("<td>")[1]).split("</td>")[0]);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				String https_url3 = "https://en"+world+".tribalwars.net/guest.php?screen=info_player&ajax=fetch_villages&player_id="+id;
				URL url3;
				HttpsURLConnection con3 = null;
				try {
					url3 = new URL(https_url3);
					con3 = (HttpsURLConnection)url3.openConnection();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					
				}
				if(con3 != null) {
					try {
						BufferedReader br = 
								new BufferedReader(
									new InputStreamReader(con3.getInputStream()));
						String input;
						while((input = br.readLine()) != null) {
							l2.addAll(getAllMatches(input,"[0-9]{3}\\|[0-9]{3}"));
						}
						output.put(key, l);
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
				
				
			}
			output.put(key, l2);
			
		}
		return output;
	}
	
	public static List<String> getTribeVillages(String tribeId) {
		List<String> playerId = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("player.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(tribeId.equals(input.split(",")[2])) {
					playerId.add(input.split(",")[0]);
				}
			}
			br.close();
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		List<String> output = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("village.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(playerId.contains(input.split(",")[4])) {
					output.add(input.split(",")[2]+"|"+input.split(",")[3]);
				}
			}
			br.close();
		}  catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public static void main(String[] args) {
		new Download(78);
		TribeDominance td = new TribeDominance(78);
//		Map<String, List<String>> td1 = td.grabContinentTribeData();
//		System.out.println(td.getTopFifteenTribeDom(td1));
//		System.out.println(td.grabContinentTribeData());
		td.createMap();

	}

}
