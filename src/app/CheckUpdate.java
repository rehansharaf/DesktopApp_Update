package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class CheckUpdate {

    private static final Logger logger = LoggerConfig.getLogger(CheckUpdate.class);

    static Properties prop;
    static Properties prop_local;
    static FTPClient ftp;
    static String currentVersion = "";
    static String currentPath = "";
    static boolean conSuccess = false;
    static boolean needUpdate = false;

    public static void main(String[] args) throws IOException {
        logger.info("Starting application update check...");
        
        prop_local = new Properties();
      //FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
    	InputStream ip = CheckUpdate.class.getClassLoader().getResourceAsStream("config.properties");
    	prop_local.load(ip);
		currentVersion = prop_local.getProperty("currentVersion");

        SwingUtilities.invokeLater(() -> {
            JFrame loadingFrame = createLoadingFrame();
            loadingFrame.setVisible(true);
        });
    }

    private static JFrame createLoadingFrame() {
        JFrame loadingFrame = new JFrame("Loading...");
        loadingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loadingFrame.setSize(500, 450);
        loadingFrame.setLocationRelativeTo(null);

        JLabel loadingLabel = new JLabel("Checking app updates, please wait...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setForeground(Color.BLACK);
        loadingLabel.setOpaque(true);
        loadingLabel.setBackground(Color.WHITE);

        ImageIcon loadingIcon = new ImageIcon(CheckUpdate.class.getClassLoader().getResource("icon/Loading_icon.gif"));
        loadingLabel.setIcon(loadingIcon);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loadingFrame.add(loadingLabel, BorderLayout.CENTER);

        new Thread(() -> {
            try {
                needUpdate = checkForUpdates();
                conSuccess = true;
            } catch (Exception e) {
                LoggerConfig.logException(logger, "Error while checking for updates", e);
            }

            loadingFrame.dispose();

            if (conSuccess) {
                SwingUtilities.invokeLater(() -> {
                    if (needUpdate) {
                        File delFile = new File(currentPath + "/newversion.properties");
                        delFile.delete();

                        String command1 = "cmd.exe /C cd " + currentPath + " & java -jar update.jar";
                        try {
                            Runtime.getRuntime().exec(command1);
                        } catch (IOException e) {
                            LoggerConfig.logException(logger, "Failed to start update process", e);
                        }
                        System.exit(0);
                    } else {
                        File delFile = new File(currentPath + "/newversion.properties");
                        delFile.delete();
                        MainUI.executionMethod();
                    }
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    try {
                        errorFrame();
                    } catch (Exception e) {
                        LoggerConfig.logException(logger, "Error displaying error frame", e);
                    }
                });
            }
        }).start();

        return loadingFrame;
    }

    private static void errorFrame() {
        JFrame mainFrame = new JFrame("Error");
        mainFrame.setSize(500, 450);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JLabel mainLabel = new JLabel("Error Establishing Connection With Server....", SwingConstants.CENTER);
        mainFrame.add(mainLabel);

        mainFrame.setVisible(true);
    }

    private static boolean checkForUpdates() throws Exception {
        prop = new Properties();
        currentPath = System.getProperty("user.dir");
        String latestVersion = downloadCurrentVersion();
        return !currentVersion.equals(latestVersion);
    }

    private static String downloadCurrentVersion() throws Exception {
        FTPConnect();
        String Finalfilepath1 = "/config.properties";
        String Flocal = System.getProperty("user.dir") + "/newversion.properties";
        String Fremote = Finalfilepath1;

        ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
        ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);

        FileOutputStream fos = new FileOutputStream(Flocal);
        ftp.retrieveFile(Fremote, fos);
        fos.close();

        FileInputStream inputStream = new FileInputStream(Flocal);
        prop.load(inputStream);
        String updatedVersion = prop.getProperty("version");
        inputStream.close();

        return updatedVersion;
    }

    public static void FTPConnect() throws Exception {
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new java.io.PrintWriter(System.out)));
        int reply;
        String ip = prop_local.getProperty("local_host");
        ftp.connect(prop_local.getProperty("local_host"), 21);

        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }

        ftp.login(prop_local.getProperty("user2"), prop_local.getProperty("pass2"));
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }
}
