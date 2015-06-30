package com.elvensmite;

import java.awt.Color;

import com.elvensmite.MapCreators.PlayerDominance;
import com.elvensmite.MapCreators.TopODAPlayers;
import com.elvensmite.MapCreators.TopODTPlayers;
import com.elvensmite.MapCreators.TopPlayers;
import com.elvensmite.MapCreators.TopTribes;
import com.elvensmite.MapCreators.TribeDominance;

public class StartScript {
	
	public static int[] ColorMap = {Color.BLUE.getRGB(),Color.RED.getRGB(),Color.GREEN.getRGB(),Color.MAGENTA.getRGB(),new Color(238,118,0).getRGB(),Color.YELLOW.getRGB(),Color.CYAN.getRGB(),Color.GRAY.getRGB(),Color.PINK.getRGB(),Color.WHITE.getRGB(),new Color(128,0,128).getRGB(),new Color(139,69,19).getRGB(),new Color(127,0,0).getRGB(),new Color(0,100,0).getRGB(),new Color(0,128,128).getRGB()};
	public static int world = 78;
	public static String https_url = "https://en"+world+".tribalwars.net";
	
	
	public static void main(String[] args) {
		int world;
		if(args.length == 0) {
			world = 79;
		}else{
			world = Integer.parseInt(args[0]);
		}
		new Download(world);
		System.out.println(world);
		TopPlayers tp = new TopPlayers(world);
		tp.createMap();
		System.out.println("Top Players complete");
		TopTribes tt = new TopTribes(world);
		tt.createMap();
		System.out.println("Top Tribes complete");
		TopODAPlayers odap = new TopODAPlayers(world);
		odap.createMap();
		System.out.println("Top ODA Players complete");
		TopODTPlayers odtp = new TopODTPlayers(world);
		odtp.createMap();
		System.out.println("Top ODT Players complete");
		
		PlayerDominance pd = new PlayerDominance(world);
		pd.createMap();
		System.out.println("Player Dominance complete");
		
		TribeDominance td = new TribeDominance(world);
		td.createMap();
		
	}
}
