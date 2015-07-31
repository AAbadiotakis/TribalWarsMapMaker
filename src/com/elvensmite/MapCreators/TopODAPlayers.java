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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import com.elvensmite.Download;
import com.elvensmite.StartScript;

public class TopODAPlayers {
	static String world;
	static int[] ColorMap;
	
	public TopODAPlayers(String input) {
		world = input;
		ColorMap = StartScript.ColorMap;
	}
	
	public void createMap() {
		Map<String,String> idAndPlayer = getTopFifteen();
		int lowestX = 1000;
		int lowestY = 1000;
		int highestX = 0;
		int highestY = 0;
		for(String key: idAndPlayer.keySet()) {
			List<String> villageCoords = getPlayerVillages(key);
			for(String villageCoord: villageCoords) {
				int xCoord = Integer.parseInt(villageCoord.split("\\|")[0]);
				int yCoord = Integer.parseInt(villageCoord.split("\\|")[1]);
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
		
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(72,71,82));
		graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		int ranking = 0;

		for(String key: idAndPlayer.keySet()) {
			List<String> villageCoords = getPlayerVillages(key);
			for(String villageCoord: villageCoords) {
				if(ranking < ColorMap.length) {
					int xCoord = Integer.parseInt(villageCoord.split("\\|")[0]);
					int yCoord = Integer.parseInt(villageCoord.split("\\|")[1]);
					xCoord -= lowestX;
					yCoord -= lowestY;
					graphics.setColor(new Color(ColorMap[ranking]));
					graphics.fillRect(xCoord, yCoord, 4, 4);
					graphics.setColor(Color.BLACK);
					graphics.drawRect(xCoord-1,yCoord-1,5,5);
				}
			}
			ranking++;
		}
		

		
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(x%100 == 0)
					img.setRGB(x, y, Color.BLACK.getRGB());
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
		g.drawImage(img, 0, 30, width, height+30, 0, 0, img.getWidth(), img.getHeight(), null);
		String header = "Top ODA Players";
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
		for(String key: idAndPlayer.keySet()) {
			if(ranking < ColorMap.length) {
				fontSize = resetFont;
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				fm = g.getFontMetrics();
				if(fm.stringWidth(idAndPlayer.get(key)) > 200) {
					boolean stay = true;
					while(stay) {
						fontSize--;
						g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
						fm = g.getFontMetrics();
						if(fm.stringWidth(idAndPlayer.get(key)) < 200)
							stay = false;
					}
				}
				g.setFont(new Font("TimesRoman",Font.BOLD,fontSize));
				g.setPaint(new Color(ColorMap[ranking]));
				g.drawString(idAndPlayer.get(key),width,15+drawY);
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
		File f = new File(world+File.separator+"TopODAPlayers.jpg");
		try {
			ImageIO.write(buffered, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	public static Map<String,String> getTopFifteen() {
		Map<String,String> output = new LinkedHashMap<String,String>();
		String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=ranking&mode=kill_player&type=att";
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
							output.put(id, input.trim());
						}
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	*/
	public static Map<String,String> getTopFifteen() {
		Map<String,String> output = new LinkedHashMap<String,String>();
		for(int i = 1;i<=15;i++) {
			try {
				BufferedReader br = new BufferedReader(new FileReader("kill_att.txt"));
				String input;
				while((input = br.readLine()) != null) {
					if(Integer.toString(i).equals(input.split(",")[0])) {
						String playerName = decipherString(getPlayerName(input.split(",")[1]));
						output.put(input.split(",")[1], playerName);
					}
				}
				br.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
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
	
	public static String getPlayerName(String playerId) {
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
	
	
	
	
	
	public static void main(String[] args) {
//		new Download("78");
		TopODAPlayers odap = new TopODAPlayers("78");
		System.out.println(getTopFifteen());
		odap.createMap();
	}

}
