/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.io.Util;


public class MyWorker extends SwingWorker<Object, Object> {

    private String host;
    private String user;
    private String pass;
    private File outputFile;
    private String folder;
    private String filename;


    public MyWorker(String host, String user, String pass, File f, String category,String filename) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        outputFile = f;
        folder = category;
        this.filename = filename;
    }

    @Override
    protected Object doInBackground() throws Exception {
        
        //File f = null;

        // You're ignoring the host you past in to the constructor
        String location = outputFile.toString();

       /* try {
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

        int reply = ftpclient.getReplyCode();
        System.out.println("Received Reply from FTP Connection:" + reply);

        if (FTPReply.isPositiveCompletion(reply)) {
            System.out.println("Connected Success");
        }*/
        
        FTPClient ftp = new FTPClient();

        ftp.setBufferSize(1024000);
        int reply;
        ftp.setControlEncoding("UTF-8");
        try {
        	ftp.connect(host, 21);
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
        
        ftp.login(user, pass);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        if(host.equals("10.0.0.91"))
        	ftp.enterLocalPassiveMode();
        
        ftp.setKeepAlive(true);
        ftp.setControlKeepAliveTimeout(3000);
        ftp.setDataTimeout(3000); // 100 minutes
        ftp.setConnectTimeout(3000); // 100 minutes
        
        ftp.changeWorkingDirectory("/");
        
        System.out.println(outputFile.getName().toString());

        File f1 = new File(location);
        FileInputStream input = new FileInputStream(f1);
        // ftp.storeFile(f.getName().toString(),in);

        //ProgressMonitorInputStream is= new ProgressMonitorInputStream(getParent(), "st", in);
        //OutputStream ftpOut = ftpclient.storeFileStream(outputFile.getName().toString());
        OutputStream ftpOut = ftp.storeFileStream("/"+folder+"/"+filename);



        System.out.println(ftpOut.toString());
        //newname hereSystem.out.println(ftp.remoteRetrieve(f.toString()));
        OutputStream output = new BufferedOutputStream(ftpOut);
        CopyStreamListener listener = new CopyStreamListener() {
            public void bytesTransferred(final long totalBytesTransferred, final int bytesTransferred, final long streamSize) {

                setProgress((int) Math.round(((double) totalBytesTransferred / (double) streamSize) * 100d));

            }

            @Override
            public void bytesTransferred(CopyStreamEvent arg0) {
            }
        };

        Util.copyStream(input, output, ftp.getBufferSize(), outputFile.length(), listener);
        ftpOut.close();
        return null;

    }
}