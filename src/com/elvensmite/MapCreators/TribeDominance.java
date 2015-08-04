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
		input = input.replaceAll("\\%2A", "*");
		input = input.replaceAll("\\%2B", "+");
		input = input.replaceAll("\\%2C", ",");
		input = input.replaceAll("\\%2D", "-");
		input = input.replaceAll("\\%2E", ".");
		input = input.replaceAll("\\%2F", "/");
		input = input.replaceAll("\\%5B", "[");
		input = input.replaceAll("\\%5C", "\\");
		input = input.replaceAll("\\%5D", "]");
		input = input.replaceAll("\\%5E", "^");
		input = input.replaceAll("\\%5F", "_");
		input = input.replaceAll("\\%60", "`");
		input = input.replaceAll("\\%7B", "{");
		input = input.replaceAll("\\%7C", "|");
		input = input.replaceAll("\\%7D", "}");
		input = input.replaceAll("\\%7E", "~");
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

	public static List<String> getAllMatches(String text, String regex) {
		List<String> matches = new ArrayList<String>();
		Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
		while(m.find()) {
			matches.add(m.group(1));
		}
		return matches;
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
		
		if(output.size() > 1) {
			int tribeValue = 0;
			List<String> removeableKeys = new ArrayList<String>();
			String lastKey = null;
			for(String key: output.keySet()) {
				int tmp = getTribePointValue(key);
				if(tmp > tribeValue) {
					removeableKeys.add(lastKey);
					lastKey = key;
					tmp = tribeValue;
				}else {
					removeableKeys.add(key);
				}
			}
		}
		
		return output;
	}
	
	
	
	public static int getTribePointValue(String tribeId) {
		int output = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader("ally.txt"));
			String input;
			while((input = br.readLine()) != null) {
				if(input.split(",")[0].equals(tribeId))
					output = Integer.parseInt(input.split(",")[6]);
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
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
