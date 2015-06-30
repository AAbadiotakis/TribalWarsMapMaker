package com.elvensmite.MapCreators;

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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import com.elvensmite.StartScript;

public class PlayerDominance {
	int world;
	int[] ColorMap;
	
	public PlayerDominance(int input) {
		world = input;
		ColorMap = StartScript.ColorMap;
	}
	
	public void createMap() {
		Map<String, List<String>> continentPlayerData = grabContinentPlayerData();
		Map<String,String> TopFifteen = getTopFifteenPlayerDom(continentPlayerData);
//		Map<String,List<String>> playerVillageData = grabPlayerVillageCoords(TopFifteen);
		int lowestX = 1000;
		int lowestY = 1000;
		int highestX = 0;
		int highestY = 0;
		for(String key: TopFifteen.keySet()) {
			List<String> villageCoords = getPlayerVillages(TopFifteen.get(key));
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

		lowestX = lowestX * 100;
		lowestY = lowestY * 100;
		highestX = (highestX * 100)+100;
		highestY = (highestY * 100)+100;
		int width = highestX - lowestX;
		int height = highestY - lowestY;

		BufferedImage img = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(72,71,82));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int ranking = 0;
		
		for(String key: TopFifteen.keySet()) {
			if(ranking < ColorMap.length) {
				List<String> l = getPlayerVillages(TopFifteen.get(key));
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
			}
			ranking++;
		}
		
		//Print Continent numbers
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(x%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
				if(y%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
				if(x%100 == 0 && y%100 == 0) {
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					FontMetrics fm = graphics.getFontMetrics();
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+(y+lowestY)/100+(x+lowestX)/100, x+2, y+fm.getHeight());
				}
			}
		}
		
		//Print Player name per K
		//Print % Player Owned/total K
		for(String key : continentPlayerData.keySet()) {
			String x = ""+key.charAt(0);
			String y = ""+key.charAt(1);
			
			int yCoord = Integer.parseInt(x);
			int xCoord = Integer.parseInt(y);
			
			yCoord = yCoord*100;
			xCoord = xCoord*100;
			xCoord -= lowestX;
			yCoord -= lowestY;
			
			List<String> l = continentPlayerData.get(key);
			String player = l.get(1);
			String f = l.get(2);
			float f1 = Float.parseFloat(f);
			f1 = f1*100;
			f = String.format("%.2f", f1);
			f += "%";
			FontMetrics fm = graphics.getFontMetrics();
			int fontSize = 12;
			graphics.setFont(new Font("Dialog", Font.BOLD, fontSize));
			fm = graphics.getFontMetrics();
			if(fm.stringWidth(player) > 100) {
				boolean stay = true;
				while(stay) {
					fontSize--;
					graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
					fm = graphics.getFontMetrics();
					if(fm.stringWidth(player) < 100)
						stay = false;
				}
			}
			graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
			graphics.setPaint(new Color(0,0,0));
			fm = graphics.getFontMetrics();
			int moveX = fm.stringWidth(player);
			graphics.drawString(player,xCoord+((100-moveX)/2),yCoord+50);
			graphics.setFont(new Font("Dialog",Font.BOLD,12));
			moveX = fm.stringWidth(f);
			graphics.drawString(f,xCoord+((100-moveX)/2),yCoord+75);
		}
		
		ranking = 0;
		graphics.dispose();
		int drawY = 50;
		BufferedImage fullImage = new BufferedImage(width+200,height+30,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullImage.createGraphics();
		g.drawImage(img, 0, 30, width, height+30, 0, 0, img.getWidth(), img.getHeight(), null);
		String header = "Player Dominance";
		g.setFont(new Font("TimesRoman",Font.BOLD,21));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(header, (width/2)-((fm.stringWidth(header))/2), fm.getHeight());
		g.setFont(new Font("Dialog", Font.BOLD, 14));
		fm = graphics.getFontMetrics();
		
		//Print player Key
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
		drawY = fm.getHeight();
		for(String key: TopFifteen.keySet()) {
			if(ranking < ColorMap.length) {
				fontSize = resetFont;
				g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
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
				g.drawString(key,width,15+drawY);
				drawY += fm.getHeight();
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
		
		File f = new File("w"+world+File.separator+"PlayerDominance.jpg");
		try {
			ImageIO.write(buffered, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		List<String> topFifteenPlayers = new ArrayList<String>();
		for(String key: m.keySet()) {
			List<String> l = m.get(key);
			Float currentVal = Float.parseFloat(l.get(2));
			topFifteen.add(currentVal);
		}
		Collections.sort(topFifteen, new Comparator<Float>	() {

			@Override
			public int compare(Float arg0, Float arg1) {
				int result = Float.compare(arg1, arg0);
				return result;
			}
			
			
		});	
		for(int i=0;i<15;i++) {
			for(String key: m.keySet()) {
				List<String> l = m.get(key);
				Float currentVal = Float.parseFloat(l.get(2));
				String currentPlayer = l.get(1);
				if(currentVal.equals(topFifteen.get(i))) {
					if(!topFifteenPlayers.contains(currentPlayer)) {
						topFifteenPlayers.add(currentPlayer);
						output.put(currentPlayer, l.get(0));
					}
				}		
			}
		}		
		return output;
	}
	
	public List<String> getPlayerVillages(String id) {
		List<String> output = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("village.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(id.equals(input.split(",")[4])) {
					output.add(input.split(",")[2]+"|"+input.split(",")[3]);
				}
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
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
	
	public static void findTopContinentPlayer(int k) {
		
	}
	
	public static void main(String args[]) {
		PlayerDominance pcp = new PlayerDominance(78);
		System.out.println(pcp.grabContinentPlayerData());
//		pcp.createMap();
	}
}
