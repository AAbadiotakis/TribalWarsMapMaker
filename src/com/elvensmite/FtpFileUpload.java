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
	
	public FtpFileUpload(int world) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(host,21);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			InputStream inputStream;
			
			File localFastestNoblersFile = new File("w"+world+"/FastestNoblers.jpg");
			String remoteFastestNoblersFile = "w"+world+"/FastestNoblers.jpg";
			inputStream = new FileInputStream(localFastestNoblersFile);
			ftpClient.storeFile(remoteFastestNoblersFile, inputStream);
			inputStream.close();
			
			
			File localPlayerDominanceFile = new File("w"+world+"/PlayerDominance.jpg");
			String remotePlayerDominanceFile = "w"+world+"/PlayerDominance.jpg";
			inputStream = new FileInputStream(localPlayerDominanceFile);
			ftpClient.storeFile(remotePlayerDominanceFile, inputStream);
			inputStream.close();
			
			File localTopAvgTribesFile = new File("w"+world+"/TopAvgTribes.jpg");
			String remoteTopAvgTribesFile = "w"+world+"/TopAvgTribes.jpg";
			inputStream = new FileInputStream(localTopAvgTribesFile);
			ftpClient.storeFile(remoteTopAvgTribesFile, inputStream);
			inputStream.close();

			
			File localTopNoblingFile = new File("w"+world+"/TopNoblingTribes.jpg");
			String remoteTopNoblingFile = "w"+world+"/TopNoblingTribes.jpg";
			inputStream = new FileInputStream(localTopNoblingFile);
			ftpClient.storeFile(remoteTopNoblingFile, inputStream);
			inputStream.close();
			
			
			File localTopOdaPlayersFile = new File("w"+world+"/TopODAPlayers.jpg");
			String remoteTopOdaPlayersFile = "w"+world+"/TopODAPlayers.jpg";
			inputStream = new FileInputStream(localTopOdaPlayersFile);
			ftpClient.storeFile(remoteTopOdaPlayersFile, inputStream);
			inputStream.close();
			
			
			File localTopOdtPlayersFile = new File("w"+world+"/TopODTPlayers.jpg");
			String remoteTopOdtPlayersFile = "w"+world+"/TopODTPlayers.jpg";
			inputStream = new FileInputStream(localTopOdtPlayersFile);
			ftpClient.storeFile(remoteTopOdtPlayersFile, inputStream);
			inputStream.close();
			
			
			File localTopPlayersFile = new File("w"+world+"/TopPlayers.jpg");
			String remoteTopPlayersFile = "w"+world+"/TopPlayers.jpg";
			inputStream = new FileInputStream(localTopPlayersFile);
			ftpClient.storeFile(remoteTopPlayersFile, inputStream);
			inputStream.close();
			
			File localTopTribesFile = new File("w"+world+"/TopTribes.jpg");
			String remoteTopTribesFile = "w"+world+"/TopTribes.jpg";
			inputStream = new FileInputStream(localTopTribesFile);
			ftpClient.storeFile(remoteTopTribesFile, inputStream);
			inputStream.close();
			
			
			File localTribeDominanceFile = new File("w"+world+"/TribeDominance.jpg");
			String remoteTribeDominanceFile = "w"+world+"/TribeDominance.jpg";
			inputStream = new FileInputStream(localTribeDominanceFile);
			ftpClient.storeFile(remoteTribeDominanceFile, inputStream);
			inputStream.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
