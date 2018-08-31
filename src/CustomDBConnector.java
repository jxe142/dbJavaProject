import java.sql.*;

public class CustomDBConnector {
	public static void main(String[] args){
		connect();
	}
	
	public static  void connect(){
		try {
			//Used to connet to the db located on the local server. 
			//dbfinalproject is the name of the datbase we wish to edit and the other stuff is for the username and pass word
			Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/dbfinalproject?verifyServerCertificate=false&useSSL=true", "Joel", "Durham908");
			Statement st = con.createStatement();
			
			ResultSet myRs = st.executeQuery(null); //holds the results 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();

		}
	}

}
