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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.elvensmite.Download;
import com.elvensmite.StartScript;

public class FastestNoblers {
	static String world;
	static int[] ColorMap;
	
	
	public FastestNoblers(String input) {
		world = input;
		ColorMap = StartScript.ColorMap;
	}
	
	public void createMap() {
		Map<String,Double> m = getFastestNoblers();
		int lowestX = findLowestX();
		int lowestY = findLowestY();
		int highestX = findHighestX();
		int highestY = findHighestY();
		int ranking = 0;
		lowestX = lowestX * 100;
		lowestY = lowestY * 100;
		highestX = (highestX * 100) + 100;
		highestY = (highestY * 100) + 100;
		int width = highestX - lowestX;
		int height = highestY - lowestY;
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(72,71,82));
		graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
		ranking = 0;
		for(String key: m.keySet()) {
			if(ranking < ColorMap.length) {
				List<String> villageCoords = getPlayerVillages(key);
				for(String villageCoord: villageCoords) {
					int xCoord = Integer.parseInt(villageCoord.split("\\|")[0]);
					int yCoord = Integer.parseInt(villageCoord.split("\\|")[1]);
					xCoord -= lowestX;
					yCoord -= lowestY;
					graphics.setColor(new Color(ColorMap[ranking]));
					graphics.fillRect(xCoord, yCoord, 4, 4);
					graphics.setColor(Color.BLACK);
					graphics.drawRect(xCoord-1,yCoord-1,5,5);
				}
				ranking++;
			}
		}
		
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(x%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
				if(y%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
				if(x%100 == 0 && y%100 == 0 && x != width && y != height) {
					FontMetrics fm = graphics.getFontMetrics();
					graphics.drawString(""+(y+lowestY)/100+(x+lowestX)/100, x+2, y+fm.getHeight());
				}
				
			}
		}
		
		ranking = 0;
		graphics.dispose();
		BufferedImage fullImage = new BufferedImage(width+200,height+30,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullImage.createGraphics();
		g.drawImage(img, 0, 30, width, height+30, 0, 0, img.getWidth(), img.getHeight(), null);
		String header = "Fastest Noblers";
		g.setFont(new Font("TimesRoman", Font.BOLD, 21));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(header, (width/2) - ((fm.stringWidth(header))/2), fm.getHeight());
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		fm = g.getFontMetrics();
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
		int drawY = fm.getHeight();
		for(String key: m.keySet()) {
			if(ranking < ColorMap.length) {
				fontSize = resetFont;
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				fm = g.getFontMetrics();
				DecimalFormat df = new DecimalFormat("0.00");
				String villPerDay = df.format(m.get(key));
				String playerName = getPlayerName(key);
				if(playerName != null) {
					playerName = playerName + " ("+villPerDay+")";
					if(fm.stringWidth(playerName) > 200) {
						boolean stay = true;
						while(stay) {
							fontSize--;
							g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
							fm = g.getFontMetrics();
							if(fm.stringWidth(playerName) < 200)
								stay = false;
						}
					}
					g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
					g.setPaint(new Color(ColorMap[ranking]));
					g.drawString(playerName,width,15+drawY);
					drawY += fm.getHeight();
					ranking++;
				}
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
		File f = new File(world+File.separator+"FastestNoblers.jpg");
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
			while((input = br.readLine())!= null) {
				if(playerId.equals(input.split(",")[0])) {
					output = input.split(",")[1];
					break;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(output != null)
			return decipherString(output);
		else
			return null;
	}
	
	
	public static Map<String,Double> getFastestNoblers() {
		Map<String,ArrayList<Long>> output =  new LinkedHashMap<String,ArrayList<Long>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("conquer.txt"));
			String input;
			while((input = br.readLine()) != null) {
				String playerId = input.split(",")[2];
				Long villageNobled = Long.parseLong(input.split(",")[1]);
				if(output.containsKey(playerId)) {
					ArrayList<Long> tmp = output.get(playerId);
					tmp.set(0, tmp.get(0) + 1);
					if(tmp.get(1) > villageNobled)
						tmp.set(1,villageNobled);
					output.put(playerId, tmp);
				}else {
					ArrayList<Long> tmp = new ArrayList<Long>();
					tmp.add((long) 1);
					tmp.add(villageNobled);
					output.put(playerId, tmp);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String,Double> newOutput = new LinkedHashMap<String,Double>();
		for(String key: output.keySet()) {
			ArrayList<Long> tmp = output.get(key);
				
			Long numVillages = tmp.get(0);
			Long firstVillage = tmp.get(1);

			if(numVillages != 1) {
				Long currentTime = System.currentTimeMillis();
				currentTime = currentTime/1000;
				
				Double value = (double) ((double)numVillages/((currentTime - firstVillage)/86400));
				newOutput.put(key, value);
			}
			
		}
		
		
		newOutput = orderFastestNoblers(newOutput);
		return newOutput;
		
	}
	
	public static Map<String,Double> orderFastestNoblers(Map<String,Double> m) {
		Map<String,Double> output = new LinkedHashMap<String,Double>();
		List<Map.Entry<String, Double>> entries = new ArrayList<Map.Entry<String, Double>>(m.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> arg0,
					Entry<String, Double> arg1) {
				return arg1.getValue().compareTo(arg0.getValue());
			}
			
			
		});
		for(Entry<String,Double> entry : entries) {
			output.put(entry.getKey(), entry.getValue());
		}
		return output;
	}
	
	public static List<String> getPlayerVillages(String id) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
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
	
	public static int findLowestX() {
		int output = 10;
		for(int x = 0;x < 10;x++) {
			for(int y = 0;y < 10; y++) {
				int continent = (y*10) + x;
				int totalVillages = getTotalVillagesInContinent(continent);
				if(totalVillages > 0) {
					if(x < output)
						output = x;
				}
			}
		}
		return output;
	}
	
	public static int findLowestY() {
		int output = 10;
		for(int x = 0;x < 10;x++) {
			for(int y = 0;y < 10; y++) {
				int continent = (y*10) + x;
				int totalVillages = getTotalVillagesInContinent(continent);
				if(totalVillages > 0) {
					if(y < output)
						output = y;
				}
			}
		}
		return output;
	}
	
	public static int findHighestX() {
		int output = 0;
		for(int x = 0;x < 10;x++) {
			for(int y = 0;y < 10; y++) {
				int continent = (y*10) + x;
				int totalVillages = getTotalVillagesInContinent(continent);
				if(totalVillages > 0) {
					if(x > output)
						output = x;
				}
			}
		}
		return output;
	}
	
	public static int findHighestY() {
		int output = 0;
		for(int x = 0;x < 10;x++) {
			for(int y = 0;y < 10; y++) {
				int continent = (y*10) + x;
				int totalVillages = getTotalVillagesInContinent(continent);
				if(totalVillages > 0) {
					if(y > output)
						output = y;
				}
			}
		}
		return output;
	}
	
	public static int getTotalVillagesInContinent(int continent) {
		int y = continent/10;
		int x = continent%10;
		int output = 0;
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

	public static void main(String[] args) {
//		new Download(78);
//		FastestNoblers fn = new FastestNoblers(78);
//		fn.createMap();
		findLowestX();
		System.out.println(findLowestX());
		System.out.println(findLowestY());
		System.out.println(findHighestX());
		System.out.println(findHighestY());
//		System.out.println(getTotalVillagesInContinent(4));
	}

}
