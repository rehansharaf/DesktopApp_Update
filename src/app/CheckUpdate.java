package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

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
	
	static Properties prop;
	static FTPClient ftp;
	static String currentVersion = "1.2";
	static String currentPath = "";
	static boolean conSuccess = false;
	static boolean needUpdate = false;
	
    public static void main(String[] args) throws Exception {
    	
    	  SwingUtilities.invokeLater(() -> {
              // Create the loading frame and label
              JFrame loadingFrame = createLoadingFrame();
              loadingFrame.setVisible(true);  // Set the frame visible after setup
          });
    	
    	
    	
    }
    
    
    private static JFrame createLoadingFrame() {
        // Create the JFrame for the loading window
        JFrame loadingFrame = new JFrame("Loading...");
        loadingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loadingFrame.setSize(500, 450);
        loadingFrame.setLocationRelativeTo(null); // Center the frame

        // Create the JLabel to display the loading message
        JLabel loadingLabel = new JLabel("Checking app updates, please wait...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setForeground(Color.BLACK); // Set label text color to black

        // Set a background color for the label to ensure it's visible
        loadingLabel.setOpaque(true); // Make sure the label is opaque
        loadingLabel.setBackground(Color.WHITE); // Set the background color of the label
        
        ImageIcon loadingIcon = new ImageIcon(CheckUpdate.class.getClassLoader().getResource("icon/Loading_icon.gif"));

        //ImageIcon loadingIcon = new ImageIcon(System.getProperty("user.dir")+"/src/resources/Loading_icon.gif");
        loadingLabel.setIcon(loadingIcon);  // Set the loading GIF as the label's icon
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the GIF in the frame

        // Add the label to the frame
        loadingFrame.add(loadingLabel, BorderLayout.CENTER);

        
        // Simulate loading by using a background thread
        new Thread(() -> {
            try {
                // Simulate a time-consuming task (like loading data or initializing resources)
            	
            	needUpdate = checkForUpdates();
            	conSuccess = true;
            	
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Once the loading is complete, close the loading window
            loadingFrame.dispose();
            
            if(conSuccess) {
           
            	 // Now launch the main application window
                SwingUtilities.invokeLater(() -> {
    				
                	if(needUpdate) {
                		File delFile = new File(currentPath+"/newversion.properties");
                	    System.out.println("FilePath is "+currentPath+"/newversion.properties");
                	    boolean flag = delFile.delete();
                		
                    	//String command1 = "java -jar "+currentPath+"/"+"update.jar";
                    	String command1 = "cmd.exe /C cd "+currentPath+" & "+"java -jar update.jar";
                    	try {
							Runtime.getRuntime().exec(command1);
						} catch (IOException e) {
							e.printStackTrace();
						}
                	    System.exit(0);
                	}else {
                		
                		File delFile = new File(currentPath+"/newversion.properties");
                	    System.out.println("FilePath is "+currentPath+"/newversion.properties");
                	    boolean flag = delFile.delete();
                	    System.out.println("Flag is "+flag);
                	    MainUI.executionMethod();
                	}
    			});
                
            }else {
            	
            	 // Now launch the error window
                SwingUtilities.invokeLater(() -> {
    				try {
    					errorFrame();
    				} catch (Exception e) {
    					e.printStackTrace();
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
        mainFrame.setLocationRelativeTo(null); // Center the window

        JLabel mainLabel = new JLabel("Error Establishing Connection With Server....", SwingConstants.CENTER);
        mainFrame.add(mainLabel);

        mainFrame.setVisible(true);  // Show the main application window
		
	}
    
    
    private static boolean checkForUpdates() throws Exception {
        // Logic to check if the update is available (similar to the previous method)
    	
    	prop = new Properties();

    	currentPath = System.getProperty("user.dir");
    	
	    String latestVersion = downloadCurrentVersion();
	    
	    if(currentVersion.equals(latestVersion)) {
	    	System.out.println("Versions are equal");
	    	 return false;
	    }else {
	    	System.out.println("Current Version is outdated");
	    	return true;
	    }
	    
       
    }
    
    
    private static String downloadCurrentVersion() throws Exception {

   	     FTPConnect();
 		 String Finalfilepath1 = "/config.properties";      //Change the folder
     	 String Flocal = System.getProperty("user.dir")+"/newversion.properties";
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
    
    
    public static void FTPConnect() throws Exception{
		
    	//try {
    		
			ftp = new FTPClient();
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		    int reply;
		    ftp.connect("10.0.0.91",21);
		
		    
		    System.out.println("FTP URL is:"+ftp.getDefaultPort());
		    reply = ftp.getReplyCode();
		    if (!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();
		        throw new Exception("Exception in connecting to FTP Server");
		    }
			ftp.login("desktopapp", "rehanedex");
		    ftp.setFileType(FTP.BINARY_FILE_TYPE);
		    ftp.enterLocalPassiveMode();   
		    
    	/*}catch(Exception e) {
    		
    		JFrame mainFrame = new JFrame("Error");
            mainFrame.setSize(500, 450);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationRelativeTo(null); // Center the window

            JLabel mainLabel = new JLabel("Error Establishing Connection With Server....", SwingConstants.CENTER);
            mainFrame.add(mainLabel);

            mainFrame.setVisible(true);  // Show the main application window
    	}*/
	}

   
}