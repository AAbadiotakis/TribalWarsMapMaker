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

import com.elvensmite.Download;
import com.elvensmite.StartScript;

public class PlayerDominance {
	int world;
	int[] ColorMap;
	
	public PlayerDominance(int input) {
		world = input;
		ColorMap = StartScript.ColorMap;
	}
	/*
	public void createMap() {
		Map<String, List<String>> continentPlayerData = grabContinentPlayerData();
		Map<String,String> TopFifteen = getTopFifteenPlayerDom(continentPlayerData);
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
	*/
	public void createMap() {
		int lowestX = 1000;
		int lowestY = 1000;
		int highestX = 0;
		int highestY = 0;
		
		for(int i = 11; i < 99; i++) {
			if(getTotalVillagesInContinent(i) > 0) {
				int y = i/10;
				int x = i%10;
				if(x < lowestX)
					lowestX = x;
				if(y < lowestY)
					lowestY = y;
				if(x > highestX)
					highestX = x;
				if( y > highestY)
					highestY = y;
			}
		}

		highestX -= 1;
		highestY -= 1;
		
		int width = ((highestX * 100) + 100) - (lowestX*100);
		int height = ((highestY * 100) + 100) - (lowestY*100);

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(72,71,82));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int ranking = 0;
		
		Map<String,Long> topFifteenPlayers = findTopContinentPlayers();		
		
		
		//Print top fifteen players villages
		for(String key: topFifteenPlayers.keySet()) {
			if(ranking < ColorMap.length) {
				List<String> l = getPlayerVillages(key);
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
					graphics.drawString(""+((y/100)+ lowestY) + "" + ((x/100)+lowestX), x+2, y+fm.getHeight());
				}
			}
		}
		for(int x = lowestX;x<highestX;x++) {
			for(int y = lowestY;y<highestY;y++) {
				Map<String,Long> continentPlayer = findTopContinentPlayer(x,y);
				for(String key: continentPlayer.keySet()) {
					int yCoord = (y * 100) - (lowestY * 100);
					int xCoord = (x * 100) - (lowestY * 100);
					String playerName = getPlayerName(key);
					Long numPlayerVillages = continentPlayer.get(key);
					Double numTotalVillages = (double) getTotalVillagesInContinent((y*10) + x);
					Double percentControl = (numPlayerVillages/numTotalVillages) * 100;
					String writeLine = String.format("%.2f", percentControl) + "%";
								
					int fontSize = 12;
					graphics.setFont(new Font("Dialog", Font.BOLD, fontSize));
					FontMetrics fm = graphics.getFontMetrics();
					if(fm.stringWidth(playerName) > 100) {
						boolean stay = true;
						while(stay) {
							fontSize--;
							graphics.setFont(new Font("Dialog", Font.BOLD, fontSize));
							fm = graphics.getFontMetrics();
							if(fm.stringWidth(playerName) <= 100)
								stay = false;
						}
					} 
					graphics.setFont(new Font("Dialog",Font.BOLD,fontSize));
					graphics.setPaint(new Color(0,0,0));
					fm = graphics.getFontMetrics();
					int moveX = fm.stringWidth(playerName);
					graphics.drawString(playerName,xCoord+((100-moveX)/2),yCoord+50);
					graphics.setFont(new Font("Dialog",Font.BOLD,12));
					moveX = fm.stringWidth(writeLine);
					graphics.drawString(writeLine,xCoord+((100-moveX)/2),yCoord+75);		
				}
			}
		}
		
		ranking = 0;
		graphics.dispose();
		BufferedImage fullImage = new BufferedImage(width+200,height+30,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullImage.createGraphics();
		g.drawImage(img, 0, 30, width, height+30, 0, 0, img.getWidth(), img.getHeight(), null);
		String header = "Player Dominance";
		g.setFont(new Font("TimesRoman",Font.BOLD,21));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(header, (width/2)-((fm.stringWidth(header))/2), fm.getHeight());
		
		int fontSize = 20;
		boolean isTrue = true;
		
		while(isTrue) {
			g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
			fm = g.getFontMetrics();
			if(height - 15*fm.getHeight() > 20)
				isTrue = false;
			else
				fontSize--;
		}
		int resetFont = fontSize;
		int drawY = fm.getHeight();
		
		for(String key: topFifteenPlayers.keySet()) {
			if(ranking < ColorMap.length) {
				fontSize = resetFont;
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				fm = g.getFontMetrics();
				String playerName = getPlayerName(key);
				if(fm.stringWidth(playerName) > 200) {
					boolean stay = true;
					while(stay) {
						fontSize--;
						g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
						fm = g.getFontMetrics();
						if(fm.stringWidth(playerName) <= 200)
							stay = false;
					}
				}
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				g.setPaint(new Color(ColorMap[ranking]));
				g.drawString(playerName, width, 15+drawY);
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
	
	public String getPlayerName(String playerId) {
		String output = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("player.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(playerId.equals(input.split(",")[0]))
					output = input.split(",")[1];
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return decipherString(output);
	}
	
	public static String decipherString(String input) {
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	
	public static Map<String,Long> findTopContinentPlayers() {
		Map<String,Long> map = new LinkedHashMap<String,Long>();
		for(int x = 0; x < 10; x++) {
			for(int y = 0; y < 10; y++) {
				map.putAll(findTopContinentPlayer(x,y));
			}
		}
		List<Long> values = new ArrayList<Long>();
		for(Long entry: map.values()) {
			values.add(entry);
		}
		Collections.sort(values, new Comparator<Long> () {
			@Override
			public int compare(Long arg0, Long arg1) {
				// TODO Auto-generated method stub
				return Long.compare(arg1, arg0);
			}
		});
		Map<String,Long> output = new LinkedHashMap<String,Long>();
		for(int i=0;i<15;i++) {
			for(String key: map.keySet()) {
				if((values.get(i)).equals(map.get(key))) {
					output.put(key,map.get(key));
				}
			}
		}
		return output;
	}
	
	
	public static Map<String,Long> findTopContinentPlayer(int x, int y) {
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
					if(!playerId.equals("0")) {
//						long villagePoints = Long.parseLong(input.split(",")[5]);
						if(map.containsKey(playerId)) {
							long tmp = map.get(playerId);
							tmp += 1;
							map.put(playerId, tmp);
						}else {
							map.put(playerId, (long) 1);
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Long> values = new ArrayList<Long>();
		for(Long entry: map.values()) {
			values.add(entry);
		}
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
	
	public static void main(String args[]) {
//		PlayerDominance pcp = new PlayerDominance(80);
//		System.out.println(pcp.grabContinentPlayerData());
//		pcp.createMap();
		new Download(78);
//		System.out.println(getTotalVillagesInContinent(45));
//		new Download(78);
//		System.out.println(""+findTopContinentPlayers());
//		System.out.println(""+findTopContinentPlayer(5,3));
		PlayerDominance pd = new PlayerDominance(78);
		pd.createMap();
	}
}
