package com.elvensmite;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class StartScript {
	
	static int[] ColorMap = {Color.BLUE.getRGB(),Color.RED.getRGB(),Color.GREEN.getRGB(),Color.MAGENTA.getRGB(),new Color(238,118,0).getRGB(),Color.YELLOW.getRGB(),Color.CYAN.getRGB(),Color.GRAY.getRGB(),Color.PINK.getRGB(),Color.WHITE.getRGB(),new Color(128,0,128).getRGB(),new Color(139,69,19).getRGB(),new Color(127,0,0).getRGB(),new Color(0,0,0).getRGB(),new Color(0,128,128).getRGB()};

	public static void main(String[] args) {
		ParsePlayers pp = new ParsePlayers(78);
		Map<String, String> pp1 = pp.grabPlayerData();
		Map<String, List<String>> pp2 = pp.grabPlayerVillageCoords(pp1);
		
		ParseTribes pt = new ParseTribes(78);
		Map<String, String> pt1 = pt.grabTribeData();
		Map<String, List<String>> pt2 = pt.grabTribePlayers(pt1);
		
		ParseODA pODA = new ParseODA(78);
		Map<String, String> pODA1 = pODA.grabODAData();
		Map<String,List<String>> pODA2 = pODA.grabPlayerVillageCoords(pODA1);
		
		ParseODT pODT = new ParseODT(78);
		Map<String, String> pODT1 = pODT.grabODTData();
		Map<String, List<String>> pODT2 = pODT.grabPlayerVillageCoords(pODT1);
		
		ParseContinentPlayers pcp = new ParseContinentPlayers(78);
		Map<String, List<String>> pcp1 = pcp.grabContinentPlayerData();
		Map<String,String> pcp2 = pcp.getTopFifteenPlayerDom(pcp1);
		Map<String,List<String>> pcp3 = pcp.grabPlayerVillageCoords(pcp2);

		ParseContinentTribes pct = new ParseContinentTribes(78);
		Map<String, List<String>> pct1 = pct.grabContinentTribeData();
		Map<String, String> pct2 = pct.getTopFifteenTribeDom(pct1);
		Map<String, List<String>> pct3 = pct.grabTribePlayers(pct2);
		
		
		createTopPlayersMap(pp2);
		createTopTribesMap(pt2);
		createTopOdaPlayersMap(pODA2);
		createTopOdtPlayersMap(pODT2);
		createPlayerDominanceMap(pcp1,pcp3);
		createTribeDominanceMap(pct1,pct3);
	}
	
	
	public static void createTopPlayersMap(Map<String,List<String>> m) {
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(0,100,0));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int rgb = new Color(0,0,0).getRGB();
		int ranking = 0;
		int drawY = 50;
		// Printing grid lines
		for(int x=0;x<1000;x++) {
			for(int y=0;y<1000;y++) {
				if(x%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(y%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(x%100 == 0 && y%100 == 0) {
					FontMetrics fm = graphics.getFontMetrics();
					int drawX = fm.stringWidth(""+y/100+x/100);
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+y/100+x/100, x+2, y+drawX);
				}
			}
		}
		
		for(String key: m.keySet()) {
			if(ranking < ColorMap.length) {
			graphics.setFont(new Font("Dialog", Font.BOLD, 20));
			graphics.setPaint(new Color(ColorMap[ranking]));
			int drawX = 15;
			
			drawY+= 25;
			List<String> l = m.get(key);
			for(String str: l) {
				int xCoord = Integer.parseInt((str.split("\\|")[0]));
				int yCoord = Integer.parseInt((str.split("\\|")[1]));
				for(int x=0;x<4;x++) {
					for(int y=0;y<4;y++) {
						img.setRGB(x+xCoord, y+yCoord, ColorMap[ranking]);
					}
				}
			}
			graphics.drawString(key, drawX, drawY);
			}
			ranking++;
		}
		File f = new File("TopPlayers.jpg");
		try {
			ImageIO.write(img, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void createTopTribesMap(Map<String,List<String>> m) {
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(0,100,0));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int rgb = new Color(0,0,0).getRGB();
		int ranking = 0;
		int drawY = 50;
		int drawX = 15;
		for(int x=0;x<1000;x++) {
			for(int y=0;y<1000;y++) {
				if(x%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(y%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(x%100 == 0 && y%100 == 0) {
					FontMetrics fm = graphics.getFontMetrics();
					drawX = fm.stringWidth(""+y/100+x/100);
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+y/100+x/100, x, y+drawX);
				}
			}
		}
		
		for(String key: m.keySet()) {
			
			if(ranking < ColorMap.length) {
				graphics.setFont(new Font("Dialog", Font.BOLD, 20));
				graphics.setPaint(new Color(ColorMap[ranking]));
				graphics.drawString(key, drawX, drawY);
				drawY+= 25;
				List<String> l = m.get(key);
				for(String str: l) {
					int xCoord = Integer.parseInt((str.split("\\|")[0]));
					int yCoord = Integer.parseInt((str.split("\\|")[1]));
					for(int x=0;x<4;x++) {
						for(int y=0;y<4;y++) {
							img.setRGB(x+xCoord, y+yCoord, ColorMap[ranking]);
						}
					}	
				}
			}
			ranking++;
		}

		File f = new File("TopTribes.jpg");
		try {
			ImageIO.write(img, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void createTopOdaPlayersMap(Map<String,List<String>> m) {
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(0,100,0));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int rgb = new Color(0,0,0).getRGB();
		int ranking = 0;
		int drawY = 50;
		for(int x=0;x<1000;x++) {
			for(int y=0;y<1000;y++) {
				if(x%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(y%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(x%100 == 0 && y%100 == 0) {
					FontMetrics fm = graphics.getFontMetrics();
					int drawX = fm.stringWidth(""+y/100+x/100);
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+y/100+x/100, x, y+drawX);
				}
			}
		}
		for(String key: m.keySet()) {	
			if(ranking < ColorMap.length) {
			graphics.setFont(new Font("Dialog", Font.BOLD, 20));
			graphics.setPaint(new Color(ColorMap[ranking]));
			int drawX = 15;
			graphics.drawString(key, drawX, drawY);
			drawY+= 25;
			List<String> l = m.get(key);
			for(String str: l) {
				int xCoord = Integer.parseInt((str.split("\\|")[0]));
				int yCoord = Integer.parseInt((str.split("\\|")[1]));
				for(int x=0;x<4;x++) {
					for(int y=0;y<4;y++) {
						img.setRGB(x+xCoord, y+yCoord, ColorMap[ranking]);
					}
				}
					
			}
			}
			ranking++;
		}

		File f = new File("TopODAPlayers.jpg");
		try {
			ImageIO.write(img, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void createTopOdtPlayersMap(Map<String,List<String>> m) {
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(0,100,0));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int rgb = new Color(0,0,0).getRGB();
		int ranking = 0;
		int drawY = 50;
		for(int x=0;x<1000;x++) {
			for(int y=0;y<1000;y++) {
				if(x%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(y%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(x%100 == 0 && y%100 == 0) {
					FontMetrics fm = graphics.getFontMetrics();
					int drawX = fm.stringWidth(""+y/100+x/100);
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+y/100+x/100, x, y+drawX);
				}
			}
		}
		for(String key: m.keySet()) {	
			if(ranking < ColorMap.length) {
			graphics.setFont(new Font("Dialog", Font.BOLD, 20));
			graphics.setPaint(new Color(ColorMap[ranking]));
			FontMetrics fm = graphics.getFontMetrics();
			int drawX = 15;
			graphics.drawString(key, drawX, drawY);
			drawY+= 25;
			List<String> l = m.get(key);
			for(String str: l) {
				int xCoord = Integer.parseInt((str.split("\\|")[0]));
				int yCoord = Integer.parseInt((str.split("\\|")[1]));
				for(int x=0;x<4;x++) {
					for(int y=0;y<4;y++) {
						img.setRGB(x+xCoord, y+yCoord, ColorMap[ranking]);
					}
				}
					
			}
			}
			ranking++;
		}

		File f = new File("TopODTPlayers.jpg");
		try {
			ImageIO.write(img, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void createPlayerDominanceMap(Map<String,List<String>> m, Map<String,List<String>> m1) {
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(0,100,0));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int rgb = new Color(0,0,0).getRGB();
		for(int x=0;x<1000;x++) {
			for(int y=0;y<1000;y++) {
				if(x%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(y%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(x%100 == 0 && y%100 == 0) {
					FontMetrics fm = graphics.getFontMetrics();
					int drawX = fm.stringWidth(""+y/100+x/100);
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+y/100+x/100, x, y+drawX);
				}
			}
		}
		for(String key: m.keySet()) {
			String x = ""+key.charAt(0);
			String y = ""+key.charAt(1);
			
			int yCoord = Integer.parseInt(x);
			int xCoord = Integer.parseInt(y);
			yCoord = yCoord*100;
			xCoord = xCoord*100;

			List<String> l = m.get(key);
			String player = l.get(1);
			String f = l.get(2);
			float f1 = Float.parseFloat(f);
			f1 = f1*100;
			f = String.format("%.2f", f1);
			f+= "%";
			FontMetrics fm = graphics.getFontMetrics();
			int moveX = fm.stringWidth(player);
			graphics.setFont(new Font("Dialog", Font.BOLD, 11));
			graphics.setPaint(new Color(255,255,255));
			graphics.drawString(player, xCoord+((100-moveX)/2), yCoord+50);
			moveX = fm.stringWidth(f);
			graphics.drawString(f, xCoord+((100-moveX)/2), yCoord+75);
		}
		
		int ranking = 0;
		int drawY = 50;
		for(String key: m1.keySet()) {	
			if(ranking < ColorMap.length) {
			graphics.setFont(new Font("Dialog", Font.BOLD, 20));
			graphics.setPaint(new Color(ColorMap[ranking]));
			FontMetrics fm = graphics.getFontMetrics();
			int drawX = 15;
			graphics.drawString(key, drawX, drawY);
			drawY+= 25;
			List<String> l = m1.get(key);
			for(String str: l) {
				int xCoord = Integer.parseInt((str.split("\\|")[0]));
				int yCoord = Integer.parseInt((str.split("\\|")[1]));
				for(int x=0;x<4;x++) {
					for(int y=0;y<4;y++) {
						img.setRGB(x+xCoord, y+yCoord, ColorMap[ranking]);
					}
				}
					
			}
			}
			ranking++;
		}	

		File f = new File("PlayerDominance.jpg");
		try {
			ImageIO.write(img, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTribeDominanceMap(Map<String,List<String>> m,Map<String,List<String>> m1) {
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setPaint(new Color(0,100,0));
		graphics.fillRect(0,0,img.getWidth(),img.getHeight());
		int rgb = new Color(0,0,0).getRGB();	
		for(int x=0;x<1000;x++) {
			for(int y=0;y<1000;y++) {
				if(x%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(y%100 == 0) {
					img.setRGB(x, y, rgb);
				}
				if(x%100 == 0 && y%100 == 0) {
					FontMetrics fm = graphics.getFontMetrics();
					int drawX = fm.stringWidth(""+y/100+x/100);
					graphics.setFont(new Font("Dialog", Font.BOLD, 20));
					graphics.setPaint(new Color(255,255,255));
					graphics.drawString(""+y/100+x/100, x, y+drawX);
				}
			}
		}
		for(String key: m.keySet()) {
			String x = ""+key.charAt(0);
			String y = ""+key.charAt(1);
			
			int yCoord = Integer.parseInt(x);
			int xCoord = Integer.parseInt(y);
			yCoord = yCoord*100;
			xCoord = xCoord*100;

			List<String> l = m.get(key);
			String player = l.get(1);
			String f = l.get(2);
			float f1 = Float.parseFloat(f);
			f1 = f1*100;
			f = String.format("%.2f", f1);
			f+= "%";
			FontMetrics fm = graphics.getFontMetrics();
			int moveX = fm.stringWidth(player);
			graphics.setFont(new Font("Dialog", Font.BOLD, 11));
			graphics.setPaint(new Color(255,255,255));
			graphics.drawString(player, xCoord+((100-moveX)/2), yCoord+50);
			moveX = fm.stringWidth(f);
			graphics.drawString(f, xCoord+((100-moveX)/2), yCoord+75);
		}
		
		int ranking = 0;
		int drawY = 50;
		int drawX = 15;
		for(String key: m1.keySet()) {
			
			if(ranking < ColorMap.length) {
				graphics.setFont(new Font("Dialog", Font.BOLD, 20));
				graphics.setPaint(new Color(ColorMap[ranking]));
				graphics.drawString(key, drawX, drawY);
				drawY+= 25;
				System.out.println(key);
				List<String> l = m1.get(key);
				System.out.println(l);
				for(String str: l) {
					int xCoord = Integer.parseInt((str.split("\\|")[0]));
					int yCoord = Integer.parseInt((str.split("\\|")[1]));
					for(int x=0;x<4;x++) {
						for(int y=0;y<4;y++) {
							img.setRGB(x+xCoord, y+yCoord, ColorMap[ranking]);
						}
					}	
				}
			}
			ranking++;
		}

		File f = new File("TribeDominance.jpg");
		try {
			ImageIO.write(img, "JPEG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
