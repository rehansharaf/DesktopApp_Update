package app;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class GetAllFiles {
	
	
	
	public FTPFile[] getAllFilesFromFolder(String directory, String hostName, String username, String password) throws IOException {
			
			FTPClient ftp = new FTPClient();
			ftp = new FTPClient();

			ftp.setBufferSize(1024000);
	        int reply;
	        ftp.setControlEncoding("UTF-8");
	        try {
				ftp.connect(hostName, 21);
			}catch(java.net.ConnectException ce) {
				ErrorFrame errFrame = new ErrorFrame("Error: Establishing Connection with Server");
	        	errFrame.setVisible(true);
			}

	        reply = ftp.getReplyCode();
	        if (FTPReply.isPositiveCompletion(reply)) {
	            System.out.println("Connected Success");
	        
	        } else if (!FTPReply.isPositiveCompletion(reply)) {
	            ftp.disconnect();
	            try {
					throw new Exception("Exception in connecting to FTP Server");
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        
	        ftp.login(username, password);
	        ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        
	        if(hostName.equals("10.0.0.91"))
	        	ftp.enterLocalPassiveMode();
	        
	        ftp.setKeepAlive(true);
	        ftp.setControlKeepAliveTimeout(3000);
	        ftp.setDataTimeout(3000); // 100 minutes
	        ftp.setConnectTimeout(3000); // 100 minutes
	        
	        /*ftp.login(username, password);
	        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
	        ftp.enterLocalPassiveMode();  
	        ftp.setKeepAlive(true);
	        ftp.setControlKeepAliveTimeout(3000);
	        ftp.setDataTimeout(3000); // 100 minutes
	        ftp.setConnectTimeout(3000); // 100 minutes

	        // You're ignoring the host you past in to the constructor
			try {
				ftp.connect(hostName, 21);
			}catch(java.net.ConnectException ce) {
				ErrorFrame errFrame = new ErrorFrame("Error: Establishing Connection with Server");
	        	errFrame.setVisible(true);
			}
	        ftp.login(username, password);

	        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

	        ftp.setKeepAlive(true);
	        ftp.setControlKeepAliveTimeout(3000);
	        ftp.setDataTimeout(3000); // 100 minutes
	        ftp.setConnectTimeout(3000); // 100 minutes
	        

	        int reply = ftp.getReplyCode();
	        System.out.println("Received Reply from FTP Connection:" + reply);

	        if (FTPReply.isPositiveCompletion(reply)) {
	            System.out.println("Connected Success");
	        
	        }*/
	        
	        ftp.changeWorkingDirectory("/");
	        
	        boolean verificationFilename = false;
			FTPFile[] files = ftp.listFiles(directory);
			
			Arrays.sort(files,
				    Comparator.comparing((FTPFile remoteFile) -> remoteFile.getTimestamp())
				        .reversed());
			
			return files;

			/*for (FTPFile file : files) {
				String details = file.getName();
				//System.out.println(details);
				if (details.equals(fileName)) {
					//System.out.println("Correct Filename");
					verificationFilename = details.equals(fileName);
					assertTrue(
							"Verification Failed: The filename is not updated at the CDN end.",
							details.equals(fileName));
				}
			}*/
	        
	}

}
