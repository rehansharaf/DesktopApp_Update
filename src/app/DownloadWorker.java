/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.io.Util;


public class DownloadWorker extends SwingWorker<Object, Object> {

    private String host;
    private String user;
    private String pass;
    private File inputFile;
    private String folder;
    private String filename;
    private String localdirPath;


    public DownloadWorker(String host,String user, String pass, File filename,String category, File localDir) {
        
    	this.host = host;
        this.user = user;
        this.pass = pass;
        localdirPath = localDir.getAbsolutePath();
        
        inputFile = filename;
        folder = category;
    }

    @Override
    protected Object doInBackground() throws Exception {
    		
        
       /* ftpclient = new FTPClient();
        try {
        	ftpclient.connect(host, 21);
        }catch(java.net.ConnectException ce) {
			ErrorFrame errFrame = new ErrorFrame("Error: Establishing Connection with Server");
        	errFrame.setVisible(true);
		}
        
        ftpclient.login(user, pass);
        ftpclient.setFileType(FTPClient.BINARY_FILE_TYPE);

        ftpclient.setKeepAlive(true);
        ftpclient.setControlKeepAliveTimeout(3000);
        ftpclient.setDataTimeout(3000); // 100 minutes
        ftpclient.setConnectTimeout(3000); // 100 minutes


        //ftpclient.changeWorkingDirectory("/");

        int reply1 = ftpclient.getReplyCode();
        System.out.println("Received Reply from FTP Connection:" + reply1);

        if (FTPReply.isPositiveCompletion(reply1)) {
            System.out.println("Connected Success");
        }*/
    	
    	FTPClient ftp = new FTPClient();

        ftp.setBufferSize(1024000);
        ftp.setControlEncoding("UTF-8");
        try {
        	ftp.connect(host, 21);
		}catch(java.net.ConnectException ce) {
			ErrorFrame errFrame = new ErrorFrame("Error: Establishing Connection with Server");
        	errFrame.setVisible(true);
		}

        int reply1 = ftp.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply1)) {
            System.out.println("Connected Success");
        
        } else if (!FTPReply.isPositiveCompletion(reply1)) {
        	ftp.disconnect();
            try {
				throw new Exception("Exception in connecting to FTP Server");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        ftp.login(user, pass);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        if(host.equals("10.0.0.91"))
        	ftp.enterLocalPassiveMode();
        
        ftp.setKeepAlive(true);
        ftp.setControlKeepAliveTimeout(3000);
        ftp.setDataTimeout(3000); // 100 minutes
        ftp.setConnectTimeout(3000); // 100 minutes
        
        
        ftp.changeWorkingDirectory("/");

        System.out.println(inputFile.getName().toString());
        
        ftp.sendCommand("SIZE", "/"+folder+"/"+inputFile.getName());
    	String reply = ftp.getReplyString();
    	reply = reply.substring(4);
    	reply = reply.trim();
    	long size = Long.parseLong(reply);
    	//long fileSizeInKB = size / 1024;

        InputStream input = null;
        try {
        	
        	
             //input = ftpclient.storeFileStream(folder+"/"+inputFile.getName());
             input = ftp.retrieveFileStream(folder+"/"+inputFile.getName());
             
         	

        }catch(Throwable e) {
        	System.out.println(e.getMessage());
        }
        // ftp.storeFile(f.getName().toString(),in);

        //ProgressMonitorInputStream is= new ProgressMonitorInputStream(getParent(), "st", in);
        //OutputStream ftpOut = ftpclient.storeFileStream(outputFile.getName().toString());
        FileOutputStream ftpOut = new FileOutputStream(localdirPath+"/"+inputFile.getName());

       

        //newname hereSystem.out.println(ftp.remoteRetrieve(f.toString()));
        OutputStream output = new BufferedOutputStream(ftpOut);
        CopyStreamListener listener = new CopyStreamListener() {
            public void bytesTransferred(final long totalBytesTransferred, final int bytesTransferred, final long streamSize) {

            	setProgress((int) Math.round(((double) totalBytesTransferred / (double) streamSize)  * 100d));

            }

            @Override
            public void bytesTransferred(CopyStreamEvent arg0) {
            }
        };

       
        try {
        	if(size == 0)
                setProgress((int) Math.round((double)100d));
        	else
        		Util.copyStream(input, ftpOut, ftp.getBufferSize(), size  , listener);
        }catch(Throwable e) {
        	System.out.println(e.getMessage());
        }
        ftpOut.close();
        input.close();
        ftp.disconnect();
        return null;

    }
}