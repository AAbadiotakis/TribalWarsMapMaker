package com.elvensmite;

import java.awt.Color;
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
			}
		}
		int ranking = 0;
		for(String key: pp2.keySet()) {
			if(ranking < ColorMap.length) {
			List<String> l = pp2.get(key);
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
		File f = new File("TestPic.jpg");
		try {
			ImageIO.write(img, "JPEG", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
