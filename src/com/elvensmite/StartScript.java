package com.elvensmite;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class StartScript {

	public static void main(String[] args) {
		ParsePlayers pp = new ParsePlayers(78);
		Map<String, String> pp1 = pp.grabPlayerData();
		Map<String, List<String>> pp2 = pp.grabPlayerVillageCoords(pp1);
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
		int rgb = new Color(255,255,255).getRGB();
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
			ranking++;
			if(ranking == 1) {
				int rank1 = new Color(0,0,255).getRGB();
				List<String> l = pp2.get(key);
				for(String str: l) {
					
					int xCoord = Integer.parseInt((str.split("\\|")[0]));
					int yCoord = Integer.parseInt((str.split("\\|")[0]));
					
					System.out.println(xCoord+"|"+yCoord);
					img.setRGB(xCoord, yCoord, rank1);
					img.setRGB(xCoord-1, yCoord, rank1);
					img.setRGB(xCoord+1, yCoord, rank1);
					img.setRGB(xCoord, yCoord-1, rank1);
					img.setRGB(xCoord, yCoord+1, rank1);
				}
			}
			
			
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
