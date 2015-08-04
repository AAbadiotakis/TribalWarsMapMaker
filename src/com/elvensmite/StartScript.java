package com.elvensmite;

import java.awt.Color;

import com.elvensmite.MapCreators.FastestNoblers;
import com.elvensmite.MapCreators.PlayerDominance;
import com.elvensmite.MapCreators.TopAvgTribes;
import com.elvensmite.MapCreators.TopNobling;
import com.elvensmite.MapCreators.TopODAPlayers;
import com.elvensmite.MapCreators.TopODTPlayers;
import com.elvensmite.MapCreators.TopPlayers;
import com.elvensmite.MapCreators.TopTribes;
import com.elvensmite.MapCreators.TribeDominance;

public class StartScript {
	
	public static int[] ColorMap = {Color.BLUE.getRGB(),Color.RED.getRGB(),Color.GREEN.getRGB(),Color.MAGENTA.getRGB(),new Color(238,118,0).getRGB(),Color.YELLOW.getRGB(),Color.CYAN.getRGB(),Color.GRAY.getRGB(),Color.PINK.getRGB(),Color.WHITE.getRGB(),new Color(128,0,128).getRGB(),new Color(139,69,19).getRGB(),new Color(127,0,0).getRGB(),new Color(0,100,0).getRGB(),new Color(0,128,128).getRGB()};

	public StartScript(String world, String webUrl) {
		new Download(world,webUrl);
		
		FastestNoblers fn = new FastestNoblers(world);
		fn.createMap();
		
		PlayerDominance pd = new PlayerDominance(world);
		pd.createMap();
		
		TopAvgTribes tat = new TopAvgTribes(world);
		tat.createMap();
		
		TopNobling tn = new TopNobling(world);
		tn.createMap();
		
		TopODAPlayers odap = new TopODAPlayers(world);
		odap.createMap();
		
		TopODTPlayers odtp = new TopODTPlayers(world);
		odtp.createMap();
		
		TopPlayers tp = new TopPlayers(world);
		tp.createMap();
		
		TopTribes tt = new TopTribes(world);
		tt.createMap();
		
		TribeDominance td = new TribeDominance(world);
		td.createMap();

	}
	
	
	public static void main(String[] args) {
//		new StartScript("en78","tribalwars.net");
//		new FtpFileUpload("en78");
//		new StartScript("en81","tribalwars.net");
//		new FtpFileUpload("en81");
		new StartScript("ts5","tribalwarsmasters.net");
		new FtpFileUpload("ts5");
		new StartScript("sv30","tribalwars.se");
		new FtpFileUpload("sv30");
		new StartScript("sv29","tribalwars.se");
		new FtpFileUpload("sv29");
		new StartScript("sv28","tribalwars.se");
		new FtpFileUpload("sv28");
//		new StartScript(78);
//		new FtpFileUpload(78);
//		new StartScript(79);
//		new FtpFileUpload(79);
//		new StartScript(80);
//		new FtpFileUpload(80);
//		new StartScript(81);
//		new FtpFileUpload(81);
		
	}
}
