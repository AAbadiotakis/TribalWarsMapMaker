package com.elvensmite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpFileUpload {
	static String host = "";
	static String user = "";
	static String pass = "";
	static String filePath = "/w78/TopPlayers.jpg";
	static String uploadPath="w78/TopPlayers.jpg";
	
	public FtpFileUpload(String world) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(host,21);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			InputStream inputStream;
			
			ftpClient.makeDirectory(world);
			
			File localFastestNoblersFile = new File(world+"/FastestNoblers.jpg");
			String remoteFastestNoblersFile = world+"/FastestNoblers.jpg";
			inputStream = new FileInputStream(localFastestNoblersFile);
			ftpClient.storeFile(remoteFastestNoblersFile, inputStream);
			inputStream.close();
			
			
			File localPlayerDominanceFile = new File(world+"/PlayerDominance.jpg");
			String remotePlayerDominanceFile = world+"/PlayerDominance.jpg";
			inputStream = new FileInputStream(localPlayerDominanceFile);
			ftpClient.storeFile(remotePlayerDominanceFile, inputStream);
			inputStream.close();
			
			File localTopAvgTribesFile = new File(world+"/TopAvgTribes.jpg");
			String remoteTopAvgTribesFile = world+"/TopAvgTribes.jpg";
			inputStream = new FileInputStream(localTopAvgTribesFile);
			ftpClient.storeFile(remoteTopAvgTribesFile, inputStream);
			inputStream.close();

			
			File localTopNoblingFile = new File(world+"/TopNoblingTribes.jpg");
			String remoteTopNoblingFile = world+"/TopNoblingTribes.jpg";
			inputStream = new FileInputStream(localTopNoblingFile);
			ftpClient.storeFile(remoteTopNoblingFile, inputStream);
			inputStream.close();
			
			
			File localTopOdaPlayersFile = new File(world+"/TopODAPlayers.jpg");
			String remoteTopOdaPlayersFile = world+"/TopODAPlayers.jpg";
			inputStream = new FileInputStream(localTopOdaPlayersFile);
			ftpClient.storeFile(remoteTopOdaPlayersFile, inputStream);
			inputStream.close();
			
			
			File localTopOdtPlayersFile = new File(world+"/TopODTPlayers.jpg");
			String remoteTopOdtPlayersFile = world+"/TopODTPlayers.jpg";
			inputStream = new FileInputStream(localTopOdtPlayersFile);
			ftpClient.storeFile(remoteTopOdtPlayersFile, inputStream);
			inputStream.close();
			
			
			File localTopPlayersFile = new File(world+"/TopPlayers.jpg");
			String remoteTopPlayersFile = world+"/TopPlayers.jpg";
			inputStream = new FileInputStream(localTopPlayersFile);
			ftpClient.storeFile(remoteTopPlayersFile, inputStream);
			inputStream.close();
			
			File localTopTribesFile = new File(world+"/TopTribes.jpg");
			String remoteTopTribesFile = world+"/TopTribes.jpg";
			inputStream = new FileInputStream(localTopTribesFile);
			ftpClient.storeFile(remoteTopTribesFile, inputStream);
			inputStream.close();
			
			
			File localTribeDominanceFile = new File(world+"/TribeDominance.jpg");
			String remoteTribeDominanceFile = world+"/TribeDominance.jpg";
			inputStream = new FileInputStream(localTribeDominanceFile);
			ftpClient.storeFile(remoteTribeDominanceFile, inputStream);
			inputStream.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
