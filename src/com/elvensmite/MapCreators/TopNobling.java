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
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.elvensmite.StartScript;

public class TopNobling {
	
	static String world;
	static int[] ColorMap;
	
	
	
	public TopNobling(String input) {
		world = input;
		ColorMap = StartScript.ColorMap;
	}
	
	public void createMap() {
		Map<String,String> nobledVillages = getNobledVillages();
		Map<String,Integer> topNoblingTribes = getTopNoblingTribes();
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
		for(String key: topNoblingTribes.keySet()) {
			if(ranking >= ColorMap.length)
				break;
			if(ranking < ColorMap.length && !(key.equals("0"))) {
				List<String> villageCoords = getTribeVillages(key);
				for(String villageCoord: villageCoords) {
					int xCoord = Integer.parseInt(villageCoord.split("\\|")[0]);
					int yCoord = Integer.parseInt(villageCoord.split("\\|")[1]);
					xCoord -= lowestX;
					yCoord -= lowestY;
					Color color = new Color(ColorMap[ranking]);
					color = color.darker();
					graphics.setColor(color);
					graphics.fillRect(xCoord-2, yCoord-2, 4, 4);
					graphics.setColor(Color.BLACK);
					graphics.drawRect(xCoord-3,yCoord-3,5,5);
				}
				
				for(String nobledKey: nobledVillages.keySet()) {
					String value = nobledVillages.get(nobledKey);
					String tribeId = getTribeOfPlayer(value);
					if(tribeId == null || key == null) {
						
					}else if(key.equals("0") || tribeId.equals("0") || tribeId.isEmpty()) {
						
					}else if(tribeId.equals(key)) {
						String villageCoord = getVillageCoords(nobledKey);
						if(villageCoord == null) {
							
						}else {
							int xCoord = Integer.parseInt(villageCoord.split("\\|")[0]);
							int yCoord = Integer.parseInt(villageCoord.split("\\|")[1]);
							xCoord -= lowestX;
							yCoord -= lowestY;
							Color c = new Color(ColorMap[ranking]);
							c = c.brighter();
							graphics.setColor(c);
							graphics.fillRect(xCoord-5,yCoord-5,9,9);

							graphics.setColor(Color.BLACK);
							graphics.drawRect(xCoord-6,yCoord-6,10,10);
						}

					}
				}
				ranking++;
			}
		}
		
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(x%100 == 0)
					img.setRGB(x, y, Color.black.getRGB());
				if(y%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
				if(x%100 == 0 && y%100 == 0) {
					FontMetrics fm = graphics.getFontMetrics();
					graphics.drawString(""+(y+lowestY)/100+(x+lowestX)/100, x+2, y+fm.getHeight());
				}
			}
		}
		
		ranking = 0;
		graphics.dispose();
		BufferedImage fullImage = new BufferedImage(width+200,height+30,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = fullImage.createGraphics();
		g.drawImage(img,0,30,width, height+30, 0, 0, img.getWidth(), img.getHeight(), null);
		String header = "Top Nobling Tribes";
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
			if(height - 15 * fm.getHeight() > 20) {
				isTrue = false;
			}else {
				fontSize--;
			}
		}
		int resetFont = fontSize;
		int drawY = fm.getHeight();
		for(String key: topNoblingTribes.keySet()) {
			if(key.equals("0")) {

			}else if(ranking < ColorMap.length) {
				fontSize = resetFont;
				g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
				fm = g.getFontMetrics();
				String tribeName = getTribeName(key);
				tribeName = tribeName + " ("+topNoblingTribes.get(key)+")";
				if(fm.stringWidth(tribeName) > 200) {
					boolean stay = true;
					while(stay) {
						fontSize--;
						g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
						fm = g.getFontMetrics();
						if(fm.stringWidth(tribeName) < 200)
							stay = false;
					}
				}
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				g.setPaint(new Color(ColorMap[ranking]));
				g.drawString(tribeName,width,15+drawY);
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
		File f = new File(world+File.separator+"TopNoblingTribes.jpg");
		try {
			ImageIO.write(buffered, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public Map<String,String> getNobledVillages() {
		Map<String,String> output = new LinkedHashMap<String,String>();
		long getTime = System.currentTimeMillis();
		getTime = getTime - 86400000; // Set time to 1 day prior
		getTime = getTime/1000;
		try {
			BufferedReader br = new BufferedReader(new FileReader("conquer.txt"));
			String input;
			while((input = br.readLine()) != null) {
				Long nobledTime = Long.parseLong(input.split(",")[1]);
				if(nobledTime > getTime) {
					if(!(input.split(",")[2].equals(input.split(",")[3]))) {
						output.put(input.split(",")[0], input.split(",")[2]);
					}
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
		return output;
	}
	
	public Map<String,Integer> getTopNoblingTribes() {
		Map<String,String> m = getNobledVillages();
		Map<String,Integer> nobleCount = new LinkedHashMap<String,Integer>();
		for(String key: m.keySet()) {
			String playerId = m.get(key);
			try {
				BufferedReader br = new BufferedReader(new FileReader("player.txt"));
				String input;
				while((input = br.readLine()) != null) {
					if(input.split(",")[0].equals(playerId)) {
						if(nobleCount.containsKey(input.split(",")[2])) {
							nobleCount.put(input.split(",")[2], nobleCount.get(input.split(",")[2]) + 1);
						}else {
							nobleCount.put(input.split(",")[2], 1);
						}
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		nobleCount = sortByComparator(nobleCount);
		return nobleCount;
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
	
	public static String getTribeName(String tribeId) {
		String output = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("ally.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(tribeId.equals(input.split(",")[0])) {
					output = input.split(",")[2];
					break;
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return decipherString(output);
	}
	
	public static String getTribeOfPlayer(String playerId) {
		String output = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("player.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(playerId.equals(input.split(",")[0])) {
					output = input.split(",")[2];
					break;
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
	
	public static String getVillageCoords(String villageId) {
		String output = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("village.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(villageId.equals(input.split(",")[0])) {
					output = input.split(",")[2] + "|" + input.split(",")[3];
					break;
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
	
	public static String decipherString(String input) {
		input = input.replaceAll("\\%20", " ");
		input = input.replaceAll("\\%21","!");
		input = input.replaceAll("\\%22","\"");
		input = input.replaceAll("\\%23", "#");
//		input = input.replaceAll("\\%24", "$");
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
	
	private static Map<String,Integer> sortByComparator(Map<String,Integer> unsortMap) {
		List<Entry<String,Integer>> list = new LinkedList<Entry<String,Integer>>(unsortMap.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                    return o2.getValue().compareTo(o1.getValue());
            }
        });
		Map<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
		for(Entry<String,Integer> entry: list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	
	

	public static void main(String[] args) {
//		new Download(80);
//		TopNobling tn = new TopNobling(80);
//		System.out.println("getNobledVillages:");
//		System.out.println(tn.getNobledVillages());
		
//		System.out.println("getTopNoblingTribes:");
//		System.out.println(tn.getTopNoblingTribes());
		
//		tn.createMap();
//		System.out.println(System.currentTimeMillis()- 86400000);
	}

}
