package com.elvensmite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ParseTribes {
	String[] topTribes;
	int world;
	String[][] tribeAndId;

	public ParseTribes(int input) {
		world = input;
		String https_url = "https://en"+world+".tribalwars.net/guest.php?screen=ranking&mode=ally";
		URL url;
		try {
			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			createList(con);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createList(HttpsURLConnection con) {
		if(con!=null){
			try {
		 
			   BufferedReader br = 
				new BufferedReader(
					new InputStreamReader(con.getInputStream()));
		 
			   String input;
			   
			   boolean writeData = false;
			   int rankingNum = 0;
			   while ((input = br.readLine()) != null){
			      if(input.contains("<td class=\"lit-item\">1</td>")) {
			    	  rankingNum = 1;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">2</td>")) {
			    	  rankingNum = 2;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">3</td>")) {
			    	  rankingNum = 3;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">4</td>")) {
			    	  rankingNum = 4;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">5</td>")) {
			    	  rankingNum = 5;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">6</td>")) {
			    	  rankingNum = 6;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">7</td>")) {
			    	  rankingNum = 7;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">8</td>")) {
			    	  rankingNum = 8;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">9</td>")) {
			    	  rankingNum = 9;
			    	  writeData = true;
			      }else if(input.contains("<td class=\"lit-item\">10</td>")) {
			    	  rankingNum = 10;
			    	  writeData = true;
			      }else if(writeData) {
			    	  if(input.contains("</td>")) {
			    		  writeData = false;
			    	  }else if(input.contains("<") || input.contains(">")) {
			    		  // Do NOTHING
			    	  }else{
			    		  System.out.println("Rank: "+rankingNum+" "+input);
			    	  }
			      }
			   }
			   br.close();
		 
			} catch (IOException e) {
			   e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new ParseTribes(80);
	}
	
}
