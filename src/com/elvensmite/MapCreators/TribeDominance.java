package com.elvensmite.MapCreators;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
	String world;
	int[] ColorMap;
	
	public TribeDominance(String input) {
		world = input;
		ColorMap = StartScript.ColorMap;
	}
	/*
	public void createMap() {
		Map<String, List<String>> continentTribeData = grabContinentTribeData();
		Map<String, String> TopFifteen = getTopFifteenTribeDom(continentTribeData);
		
		int lowestX = 1000;
		int lowestY = 1000;
		int highestX = 0;
		int highestY = 0;
		
		for(String key: continentTribeData.keySet()) {
			List<String> list = continentTribeData.get(key);
			String tribeId = list.get(0);
			List<String> villageCoords = getTribeVillages(tribeId);
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
		
		highestX = (highestX*100) + 100;
		highestY = (highestY*100) + 100;
		
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
					graphics.setFont(new Font("Dialog", Font.BOLD, 16));
					graphics.setColor(new Color(Color.BLACK.getRGB()));
					FontMetrics fm = graphics.getFontMetrics();
//					graphics.setPaint(new Color(255,255,255));
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
	*/
	public void createMap() {
		
		int lowestX = 1000;
		int lowestY = 1000;
		int highestX = 0;
		int highestY = 0;
		
		for(int i = 11; i < 99; i ++) {
			if(getTotalVillagesInContinent(i) > 0) {
				int y = i/10;
				int x = i%10;
				if(x < lowestX)
					lowestX = x;
				if(y < lowestY)
					lowestY = y;
				if(x > highestX)
					highestX = x;
				if(y > highestY)
					highestY = y;		
			}
			
		}
		
		int width = ((highestX * 100) + 100) - (lowestX * 100);
		int height = ((highestY * 100) + 100) - (lowestY * 100);
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(72,71,82));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int ranking = 0;
		
		Map<String, Double> topFifteenTribes = findTopContinentTribes();
		
		
		//Print Top Fifteen Tribe Villages
		for(String key: topFifteenTribes.keySet()) {
			if(ranking < ColorMap.length) {
				List<String> l = getTribeVillages(key);
				for(String str: l) {
					int xCoord = Integer.parseInt((str.split("\\|")[0]));
					int yCoord = Integer.parseInt((str.split("\\|")[1]));
					xCoord -= (lowestX * 100);
					yCoord -= (lowestY * 100);
					graphics.setColor(new Color(ColorMap[ranking]));
					graphics.fillRect(xCoord-2, yCoord-2, 4, 4);
					graphics.setColor(Color.BLACK);
					graphics.drawRect(xCoord-3, yCoord-3, 5, 5);
				}
				ranking++;
			}
		}
		
		//Print Continent Numbers and Grid
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
					int xVal = ((x/100) + lowestX);
					int yVal = ((y/100) + lowestY);
					graphics.drawString(""+yVal+""+xVal, x+2, y+fm.getHeight());
				}
			}
		}
		
		for(int x = lowestX;x<highestX;x++) {
			for(int y = lowestY;y<highestY;y++) {
				Map<String,Long> continentTribe = findTopContinentTribe(x,y);
				if(continentTribe.isEmpty()) {
					
				} else {
					for(String key: continentTribe.keySet()) {
						int yCoord = (y * 100) - (lowestY * 100);
						int xCoord = (x * 100) - (lowestY * 100);
						String tribeName = getTribeName(key);
						Long numVillages = continentTribe.get(key);
						Double numTotalVillages = (double) getTotalVillagesInContinent((y * 10) + x);
						Double percentControl = (numVillages/numTotalVillages) * 100;
						String writeLine = String.format("%.2f", percentControl) + "%";
						
						int fontSize = 12;
						graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
						FontMetrics fm = graphics.getFontMetrics();
						if(fm.stringWidth(tribeName) > 100) {
							boolean stay = true;
							while(stay) {
								fontSize--;
								graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
								fm = graphics.getFontMetrics();
								if(fm.stringWidth(tribeName) <= 100)
									stay = false;
							}
						}
						
						graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
						graphics.setPaint(new Color(0,0,0));
						fm = graphics.getFontMetrics();
						int moveX = fm.stringWidth(tribeName);
						graphics.drawString(tribeName, xCoord+((100-moveX)/2), yCoord+50);
						graphics.setFont(new Font("Dialog",Font.BOLD,12));
						moveX = fm.stringWidth(writeLine);
						graphics.drawString(writeLine, xCoord+((100-moveX)/2), yCoord+75);
					}
				}

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
		
		int fontSize = 20;
		boolean isTrue = true;
		
		while(isTrue) {
			g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
			fm = g.getFontMetrics();
			if(height - 15 * fm.getHeight() > 20)
				isTrue = false;
			else
				fontSize--;
		}
		int resetFont = fontSize;
		int drawY = fm.getHeight();
		
		for(String key: topFifteenTribes.keySet()) {
			if(ranking < ColorMap.length) {
				fontSize = resetFont;
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				fm = g.getFontMetrics();
				String tribeName = getTribeName(key);
				if(fm.stringWidth(tribeName) > 200 ) {
					boolean stay = true;
					while(stay) {
						fontSize--;
						g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
						fm = g.getFontMetrics();
						if(fm.stringWidth(tribeName) <= 200)
							stay = false;
					}
				}
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				g.setPaint(new Color(ColorMap[ranking]));
				g.drawString(tribeName,  width, 15+drawY);
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
		
		File f = new File(world+File.separator+"TribeDominance.jpg");
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
	
	public static String getTribeName(String tribeId) {
		String tribeName = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("ally.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(input.split(",")[0].equals(tribeId)) {
					tribeName = input.split(",")[2];
					break;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decipherString(tribeName);
	}
	
	public static String decipherString(String input) {
		System.out.println(input);
		input = input.replaceAll("\\%20", " ");
		input = input.replaceAll("\\%21","!");
		input = input.replaceAll("\\%22","\"");
		input = input.replaceAll("\\%23", "#");
		input = input.replaceAll("\\%24", "$");
		input = input.replaceAll("\\%25", "%");
		input = input.replaceAll("\\%26", "&");
		input = input.replaceAll("\\%27","'");
		input = input.replaceAll("\\%28","(");
		input = input.replaceAll("\\%29", ")");
		input = input.replaceAll("\\+", " ");
		input = input.replaceAll("\\%7E","~");
//		input.replaceAll("", "");
		return input;
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
	
	public static int getTotalVillagesInContinent(int continent) {
		int y = continent;
		int x = continent%10;
		int output = 0;
		while (y > 10) {
			y= y/10;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader("village.txt"));
			String input;
			while((input = br.readLine()) != null) {
				int xCoord = Integer.parseInt(input.split(",")[2]);
				int yCoord = Integer.parseInt(input.split(",")[3]);
				while(xCoord > 10) {
					xCoord = xCoord/10;
				}
				while(yCoord > 10) {
					yCoord = yCoord/10;
				}
				if(xCoord == x && yCoord == y) {
					output++;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
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

	public static Map<String,Double> findTopContinentTribes() {
		Map<String,Double> map = new LinkedHashMap<String,Double>();
		for(int x = 0; x < 10; x++) {
			for(int y = 0; y < 10; y++) {
				Map<String,Long> tmp = findTopContinentTribe(x,y);
				for(String key: tmp.keySet()) {
					Double value = (double) tmp.get(key);
					Double totalVillages = (double) getTotalVillagesInContinent((y*10) + x);
					value = value/totalVillages;
					if(map.containsKey(key)) {
						Double tmpDouble = map.get(key);
						if(value > tmpDouble)
							map.put(key, value);
					}else {
						map.put(key, value);
					}
				}
			}
		}
		List<Double> values = new ArrayList<Double>();
		for(Double entry:map.values())
			values.add(entry);
		Collections.sort(values, new Comparator<Double> () {
			@Override
			public int compare(Double arg0, Double arg1) {
				// TODO Auto-generated method stub
				return Double.compare(arg1, arg0);
			}
		});
		Map<String,Double> output = new LinkedHashMap<String,Double>();
		for(int i=0;i<15;i++) {
			for(String key: map.keySet()) {
				if(values.size() > i && ((values.get(i)).equals(map.get(key)))) {
					if(!output.containsKey(key))
					output.put(key,map.get(key));
				}
			}
		}
		return output;
	}
	
	public static String findPlayerTribeId(String playerId) {
		String tribeId = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("player.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(playerId.equals(input.split(",")[0])) {
					tribeId = input.split(",")[2];
					break;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tribeId;
	}
	
	
	public static Map<String,Long> findTopContinentTribe(int x, int y) {
		Map<String,Long> map = new LinkedHashMap<String,Long>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("village.txt"));
			String input;
			while((input = br.readLine()) != null) {
				int xCoord = Integer.parseInt(input.split(",")[2]);
				int yCoord = Integer.parseInt(input.split(",")[3]);
				while(xCoord > 10) {
					xCoord = xCoord/10;
				}
				while(yCoord > 10) {
					yCoord = yCoord/10;
				}
				if(x == xCoord && y == yCoord) {
					String playerId = input.split(",")[4];
					String tribeId = findPlayerTribeId(playerId);
					if(tribeId != null && (!tribeId.equals("0"))) {
						if(map.containsKey(tribeId)) {
							long tmp = map.get(tribeId);
							tmp += 1;
							map.put(tribeId, tmp);
						} else {
							map.put(tribeId, (long) 1);
						}				
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		List<Long> values = new ArrayList<Long>();
		for(Long entry: map.values())
			values.add(entry);
			
		Collections.sort(values, new Comparator<Long> () {
			@Override
			public int compare(Long arg0, Long arg1) {
				return Long.compare(arg1, arg0);
			}
		});
		Map<String,Long> output = new LinkedHashMap<String,Long>();
		for(int i=0;i<1;i++) {
			for(String key: map.keySet()) {
				if((values.get(i)).equals(map.get(key))) {
					output.put(key, map.get(key));
				}
			}
		}
		return output;
	}
	
	
	
	
	public static void main(String[] args) {
//		new Download(78);
//		TribeDominance td = new TribeDominance(78);
//		System.out.println(""+findTopContinentTribe(5,5));
//		td.createMap();
//		System.out.println("Top Tribe: "+findTopContinentTribe(5,3));
//		System.out.println("Top Tribes: "+findTopContinentTribes());
//		Map<String, List<String>> td1 = td.grabContinentTribeData();
//		System.out.println(td.getTopFifteenTribeDom(td1));
//		System.out.println(td.grabContinentTribeData());
//		td.createMap();

	}

}
