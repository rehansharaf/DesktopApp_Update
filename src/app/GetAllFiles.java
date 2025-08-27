package app;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class GetAllFiles {

	private static final java.util.logging.Logger logger = LoggerConfig.getLogger(GetAllFiles.class);
	public FTPFile[] getAllFilesFromFolder(String directory, String hostName, String username, String password) throws IOException {

        FTPClient ftp = new FTPClient();

        ftp.setBufferSize(1024000);
        ftp.setControlEncoding("UTF-8");

        try {
            ftp.connect(hostName, 21);
        } catch (java.net.ConnectException ce) {
            ErrorFrame errFrame = new ErrorFrame("Error: Establishing Connection with Server");
            errFrame.setVisible(true);
            LoggerConfig.logException(logger,"Failed to connect to FTP host: " + hostName, ce);
            throw ce; // rethrow so caller knows
        }

        int reply = ftp.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply)) {
        	System.out.println("Connected Success");
        } else {
            ftp.disconnect();
            Exception e = new Exception("Exception in connecting to FTP Server");
            LoggerConfig.logException(logger,"FTP connection failed to " + hostName, e);
            throw new IOException(e);
        }

        ftp.login(username, password);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        if (hostName.equals("10.0.0.91")) {
            ftp.enterLocalPassiveMode();
        }

        ftp.setKeepAlive(true);
        ftp.setControlKeepAliveTimeout(3000);
        ftp.setDataTimeout(3000);
        ftp.setConnectTimeout(3000);

        ftp.changeWorkingDirectory("/");

        FTPFile[] files = ftp.listFiles(directory);
        Arrays.sort(files, Comparator.comparing(FTPFile::getTimestamp).reversed());

        return files;
    }
}
