package app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.io.Util;

public class MyWorker extends SwingWorker<Object, Object> {
	
	private static final Logger logger = LoggerConfig.getLogger(CheckUpdate.class);

    private String host;
    private String user;
    private String pass;
    private File outputFile;
    private String folder;
    private String filename;

    public MyWorker(String host, String user, String pass, File f, String category, String filename) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        outputFile = f;
        folder = category;
        this.filename = filename;
    }

    @Override
    protected Object doInBackground() throws Exception {

        String location = outputFile.toString();
        FTPClient ftp = new FTPClient();

        ftp.setBufferSize(1024000);
        ftp.setControlEncoding("UTF-8");

        try {
            ftp.connect(host, 21);
        } catch (java.net.ConnectException ce) {
            ErrorFrame errFrame = new ErrorFrame("Error: Establishing Connection with Server");
            errFrame.setVisible(true);
            LoggerConfig.logException(logger, "Failed to connect to FTP host: " + host, ce);
            throw ce; // rethrow so task fails
        }

        int reply = ftp.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply)) {
            System.out.println("Connected successfully to FTP server: " + host);
        } else {
            ftp.disconnect();
            Exception e = new Exception("Exception in connecting to FTP Server");
            LoggerConfig.logException(logger, "FTP connection failed to host: " + host, e);
            throw e;
        }

        ftp.login(user, pass);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        if (host.equals(CheckUpdate.prop_local.getProperty("local_host"))) {
            ftp.enterLocalPassiveMode();
        }

        ftp.setKeepAlive(true);
        ftp.setControlKeepAliveTimeout(3000);
        ftp.setDataTimeout(3000);
        ftp.setConnectTimeout(3000);

        ftp.changeWorkingDirectory("/");

        System.out.println("Uploading file: " + outputFile.getName());

        try (FileInputStream input = new FileInputStream(new File(location));
             OutputStream ftpOut = ftp.storeFileStream("/" + folder + "/" + filename);
             OutputStream output = new BufferedOutputStream(ftpOut)) {

            CopyStreamListener listener = new CopyStreamListener() {
                public void bytesTransferred(final long totalBytesTransferred, final int bytesTransferred, final long streamSize) {
                    setProgress((int) Math.round(((double) totalBytesTransferred / (double) streamSize) * 100d));
                }

                @Override
                public void bytesTransferred(CopyStreamEvent event) {
                    // not used
                }
            };

            Util.copyStream(input, output, ftp.getBufferSize(), outputFile.length(), listener);

        } catch (Exception e) {
        	LoggerConfig.logException(logger, "Error uploading file to FTP: " + filename, e);
            throw e;
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (Exception ex) {
                	LoggerConfig.logException(logger, "Error closing FTP connection", ex);
                }
            }
        }

        return null;
    }
}
