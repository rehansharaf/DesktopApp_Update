package app;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQueries extends javax.swing.JDialog {

	public static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	Connection myCon = null;
	Statement mySt, mySt1;
	PreparedStatement myPst, myPst1;
	ResultSet rs1, rs2, rs3, rs4;
	String myQuery1;
	String mySQL, myUName, myPswd;
	String p1, p4, p6, p7, p8, p9, p10, p11;
	private String dbhost = "10.0.0.92";
	private String dbuser = "ops";
	private String dbpass = "Operation@4%1";

	public DBQueries() throws ClassNotFoundException, SQLException {

		setModal(true);
		dbConnection();
		setModal(false);

	}

	public String getDetailsInjuredWorker(String serviceType) throws SQLException, ClassNotFoundException {

		String responseText;

		// select count(*) as count from SkypeCDRBackLog.EAS_File where Status != ?

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.EAS_File where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int injuredWorkerCount = rs1.getInt("count");

		if (injuredWorkerCount == 0) {
			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select FileID, TotalADJs, Filename from SkypeCDRBackLog.EAS_File where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("FileID");
			int totalCount = rs1.getInt("TotalADJs");
			String requestFilename = rs1.getString("Filename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(1) as count from SkypeCDRBackLog.EAS_FileADJs where FileID = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsCandR(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'CandR' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int injuredWorkerCount = rs1.getInt("count");

		if (injuredWorkerCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'CandR' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from SkypeCDRBackLog.candr_details where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsConexemDataFetchAll(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.conexemdoc_list_dataFetch where status is null and type = 'All' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int conexemCount = rs1.getInt("count");

		if (conexemCount == 0)

			return "Currently No File is Processing";

		else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, Filename, count from SkypeCDRBackLog.conexemdoc_list_dataFetch "
					+ "where status is null and type = 'All' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("count");
			String requestFilename = rs1.getString("Filename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from (select distinct patient_id from SkypeCDRBackLog.conexem_datafetch_detail where fileid = ?) t ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			return responseText;
		}

	}
	
	public String getDetailsConexemDataFetchDesert(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.conexem_desert_dataFetch where status is null and type = 'All' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int conexemCount = rs1.getInt("count");

		if (conexemCount == 0)

			return "Currently No File is Processing";

		else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, Filename, count from SkypeCDRBackLog.conexem_desert_dataFetch "
					+ "where status is null and type = 'All' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("count");
			String requestFilename = rs1.getString("Filename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from (select distinct patient_id from SkypeCDRBackLog.conexem_desert_datafetch_detail where fileid = ?) t ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			return responseText;
		}

	}

	public String getDetailsConexemDataFetchRFA(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.conexemdoc_list_dataFetch where status is null and type = 'RFA' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int conexemCount = rs1.getInt("count");

		if (conexemCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, Filename, count from SkypeCDRBackLog.conexemdoc_list_dataFetch "
					+ "where status is null and type = 'RFA' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("count");
			String requestFilename = rs1.getString("Filename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from (select distinct patient_id from SkypeCDRBackLog.conexem_datafetch_detail where fileid = ?) t ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public void closeDBConnection() throws SQLException {

		myCon.close();
	}

	private void dbConnection() {
		
	  try {	
		mySQL = "jdbc:mysql://" + dbhost + ":3306/SkypeCDRBackLog?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
		Class.forName(dbClassName);
		myCon = DriverManager.getConnection(mySQL,dbuser,dbpass);
	  }catch(Exception sqe) {
		  
		  ErrorFrame erFrame = new ErrorFrame("Error: Establishing Connection with Database");
		  erFrame.setVisible(true);
		  sqe.printStackTrace();
	  }
		
		
		/*mySQL = "jdbc:mysql://" + dbhost + ":3306/SkypeCDRBackLog?autoReconnect=true&useSSL=false";
		try {
			myCon = DriverManager.getConnection(mySQL, dbuser, dbpass);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		   
		try {
			Class.forName(dbClassName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			myCon = DriverManager.getConnection(mySQL,myUName,myPswd);
		} catch (SQLException e1) {
			ErrorFrame erFrame = new ErrorFrame("Error: Establishing Connection with Database");
			erFrame.setVisible(true);
			e1.printStackTrace();
		}*/

		/*try {

			mySt1 = myCon.createStatement();
			myQuery1 = "select * From SkypeCDRBackLog.mergepdf2 limit 1 ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

		} catch (Exception e) {

			mySQL = "jdbc:mysql://" + dbhost + ":3306/SkypeCDRBackLog?autoReconnect=true&useSSL=false";
			try {
				Class.forName(dbClassName);
			} catch (ClassNotFoundException el) {
				e.printStackTrace();
			}
			try {
				myCon = DriverManager.getConnection(mySQL, dbuser, dbpass);
			} catch (SQLException el) {

				ErrorFrame erFrame = new ErrorFrame("Error: Establishing Connection with Database");
				erFrame.setVisible(true);

				e.printStackTrace();
			}

		}*/

	}

	public String getDetailsFetchHearing1(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.fetchhearingdata where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int fetchHearingCount = rs1.getInt("count");

		if (fetchHearingCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, RequestFilename, ADJCounts from SkypeCDRBackLog.fetchhearingdata where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from SkypeCDRBackLog.fetchhearingdata_details where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsDocucent(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.Docucent_Requests where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int fetchHearingCount = rs1.getInt("count");

		if (fetchHearingCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, Filename,Total from SkypeCDRBackLog.Docucent_Requests where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("Total");
			String requestFilename = rs1.getString("Filename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from SkypeCDRBackLog.Docucent_Detail where FileID = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsDocMerging(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count From SkypeCDRBackLog.mergepdf2 where processed is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int mergeCount = rs1.getInt("count");

		if (mergeCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count From SkypeCDRBackLog.mergepdf2 ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int totalCount = rs1.getInt("count");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count From SkypeCDRBackLog.mergepdf2 where processed is not null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Processing File Have Total Records " + totalCount + " & Processed Records Count is "
					+ processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsDocucentPOS(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DocucentGetPOS' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int injuredWorkerCount = rs1.getInt("count");

		if (injuredWorkerCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DocucentGetPOS' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from SkypeCDRBackLog.Docucent_GetPOSDetail where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsDocsDownloadDDM(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'downloadDocsDDM' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int injuredWorkerCount = rs1.getInt("count");

		if (injuredWorkerCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'downloadDocsDDM' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from SkypeCDRBackLog.downloadDocsDDM where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsSBRRequest(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'SBRDoc' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int injuredWorkerCount = rs1.getInt("count");

		if (injuredWorkerCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'SBRDoc' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from SkypeCDRBackLog.sbrdocdetail where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsSupDecRequest(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.DelarationFileList where Status is null and ServiceName = ? ;";
		myPst = myCon.prepareStatement(myQuery1);
		myPst.setString(1, "SupDec");
		rs1 = myPst.executeQuery();
		rs1.next();

		int supDecCount = rs1.getInt("count");

		if (supDecCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select FileID, ADJCounts , FileName from SkypeCDRBackLog.DelarationFileList where Status is null and ServiceName = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setString(1, "SupDec");
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("FileID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("FileName");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from (select distinct adjnumber from DeclarationFileADJs "
					+ "where FileID = ?) t ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsSupDecAllParty(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.DelarationFileList where Status is null and ServiceName = ? ;";
		myPst = myCon.prepareStatement(myQuery1);
		myPst.setString(1, "SupDecAllParty");
		rs1 = myPst.executeQuery();
		rs1.next();

		int supDecCount = rs1.getInt("count");

		if (supDecCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select FileID, ADJCounts , FileName from SkypeCDRBackLog.DelarationFileList where Status is null and ServiceName = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setString(1, "SupDecAllParty");
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("FileID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("FileName");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from (select distinct adjnumber from DeclarationFileADJs "
					+ "where FileID = ?) t ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsSupDecAllParty2(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.DelarationFileList2 where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int supDecCount = rs1.getInt("count");

		if (supDecCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select FileID, ADJCounts , FileName from SkypeCDRBackLog.DelarationFileList2 where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("FileID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("FileName");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from (select distinct adjnumber from DeclarationFileADJs2 "
					+ "where FileID = ?) t ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsResizePDF(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'ResizePDF' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'ResizePDF' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from SkypeCDRBackLog.downloadedDocs where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsRuntimeHCFA(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadHCFA_Study' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadHCFA_Study' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from hcfadetails where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsRuntimeHCFA_AllStudy(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadHCFA_AllStudy' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadHCFA_AllStudy' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from hcfadetails where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsRuntimeInterpretingBill(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DowloadInterpetingBill' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DowloadInterpetingBill' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from InterpretingBill where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsRuntimeLedger(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadLedger' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadLedger' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from ledgerdetails where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsRuntimeLedger_AllStudy(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadLedger_AllStudy' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DownloadLedger_AllStudy' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from ledgerdetails where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsGetPDFPageNo(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'GetPDFPageNo' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'GetPDFPageNo' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from getpdfno_detail where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsInterpretingBill_CopyRecord_ML(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'Copy_Record_ML' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'Copy_Record_ML' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from copyrecord_detail where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsLienFileStatus(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.lienfiling where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFileName from SkypeCDRBackLog.lienfiling where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFileName");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from (select distinct ADJNumber from SkypeCDRBackLog.liendetails where FileID = ?) t;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsLienClaimantProvOnly(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'LienMultipleWithProvider' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'LienMultipleWithProvider' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from liendetails_withoutADJ where FileID = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsMedflowDocsDownload(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DocDownloadMed' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'DocDownloadMed' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from downloadedDocs where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsPDFValidity(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'PDFValidity' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'PDFValidity' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from pdfvalidity_detail where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsFetchHearingReq2(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.fetchhearingdata2 where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.fetchhearingdata2 where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from fetchhearingdata_details2 where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsHearingTestReq(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.fetchhearingdata_old where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.fetchhearingdata_old where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from fetchhearingdata_details_old where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsHearingTestReq2(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.fetchhearingdata_old2 where Status is null ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.fetchhearingdata_old2 where Status is null ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from fetchhearingdata_details_old2 where Fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsRequestDocNameDocucent(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'GetDocName_Docucent' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'GetDocName_Docucent' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from docname_docucent_detail where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsBulkEmail(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'BulkEmail' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'BulkEmail' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(*) as count from bulkEmail where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	}

	public String getDetailsConexemCandRPosting(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'Conexem_C&R' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'Conexem_C&R' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(1) as count from conexem_commentposting where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	
		
	}

	public String getDetailsFaxReceiptDownload(String serviceType) throws SQLException {

		String responseText;

		mySt1 = myCon.createStatement();
		myQuery1 = "select count(*) as count from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'FaxReceipt_Download' ;";
		myPst = myCon.prepareStatement(myQuery1);
		rs1 = myPst.executeQuery();
		rs1.next();

		int resizePDFCount = rs1.getInt("count");

		if (resizePDFCount == 0) {

			closeDBConnection();
			return "Currently No File is Processing";

		}else {

			mySt1 = myCon.createStatement();
			myQuery1 = "select ID, ADJCounts , RequestFilename from SkypeCDRBackLog.edexcrawler where Status is null and ServiceName = 'FaxReceipt_Download' ;";
			myPst = myCon.prepareStatement(myQuery1);
			rs1 = myPst.executeQuery();
			rs1.next();

			int fileid = rs1.getInt("ID");
			int totalCount = rs1.getInt("ADJCounts");
			String requestFilename = rs1.getString("RequestFilename");

			mySt1 = myCon.createStatement();
			myQuery1 = "select count(1) as count from Ringcentral_FaxReceipt where fileid = ? ;";
			myPst = myCon.prepareStatement(myQuery1);
			myPst.setInt(1, fileid);
			rs1 = myPst.executeQuery();
			rs1.next();

			int processCount = rs1.getInt("count");

			responseText = "Filename " + requestFilename + " Have Total Records " + totalCount
					+ " & Processed Records Count is " + processCount;
			
			closeDBConnection();
			return responseText;
		}

	
	}
}
