import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;

/** This class implements the connection to the database.  It is
    a modified version of the provide JDBC sample code. */
public class DBConnector {

    /** The connection object (connects to the database) */
    Connection connection;
    
    /** Turns on additional debugging output.  Set to false to
	restrict output. */
    boolean debug = true;

    /** Contructor which logs into the database.
	@param username Your group's SQL server username
	@param password Your group's SQL server password
     */
    public DBConnector(String username, String password) {
	if (debug) { 
	    System.out.println("Trying to connect to the database");
	}
	login(username, password);
	if (debug) {
	    System.out.println("Connected to the database");
	}
    }
    
    /** This ensures that the connection will eventually get closed. */
    public void finalize() {
	this.close();
    }
    
    /** This is a method used to log into the database that is in effect create
	connection object. */
    public void login(String username, String password) {
	/*final String host="DESKTOP-KOQE1IS";
	final String port="1433";
	final String driver="jtds:sqlserver";
	final String URL="jdbc:"+driver+"://"+host+":"+port+";"; */
	Properties prop; 
	try {
	    prop = new Properties();
	    prop.put("user", username);
	    prop.put("password", password);
	    // This only works because the database and the username 
	    // are the same.
	    prop.put("database", username); 
	    Class c =Class.forName("net.sourceforge.jtds.jdbc.Driver");
	    DriverManager.registerDriver((Driver)c.newInstance());
	   // connection = DriverManager.getConnection(URL, prop);
	    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbfinalproject?verifyServerCertificate=false&useSSL=true", "Joel", "Durham908");
	}
	catch(ClassNotFoundException e) {
	    System.err.println("Cannot load Driver");
	    System.err.println(e.getMessage());
	    System.exit(1);
	}
	catch(SQLException e) {
	    System.err.println("Cannot Connect to database.");
	    e.printStackTrace();
	    System.exit(1);
	}
	catch (Exception e) {
	    System.err.println("Unexpected error.");
	    System.err.println(e.getMessage());
	    System.exit(0);
	}
    }

    /**
     * This method queries the database for the given query
     * @param query the query String
     */
    public ResultSet query(String query) {
	ResultSet rs;
	try {
	    Statement stat = connection.createStatement();
	    rs = stat.executeQuery(query);
	} catch (Exception e) {
	    if (debug) {
		System.err.println("Error executing query: "+query);
		e.printStackTrace();
	    } else {
		System.err.println("Error in input.");
	    }
	    return null;
	}
	return rs;
	// Now you iterate through this result set using a while loop and
	// the next() function of the Result Set object.
	// The result is extracted from the set using the various get methods.
	
    }

    /** This method is for running queries which change the database 
	without returning any results. 
	@param query the query String
    */
    public void changingQuery(String query) {
	try {
	    Statement stat = connection.createStatement();
	    stat.executeUpdate(query);
	} catch (Exception e) {
	    if (debug) {
		System.err.println("Error executing changing query: "+query);
		e.printStackTrace();
	    } else {
		System.err.println("Error in input.");
	    }
	}
	return;
    }

    /** This method returns a prepared query for your existing connection.
	It is still up to your code to fill in the data values of the 
	query and to actually execute it. */
    public PreparedStatement prepareQuery(String query) {
	try {
	    return connection.prepareStatement(query); 
	} catch (Exception e) {
	    if (debug) {
		System.err.println("Error creating prepared statement:" + query);
		e.printStackTrace();
	    } else {
		System.err.println("Error in input.");
	    }
	    return null;
	}
    }


    /**
     * This method closes the connection to the database
     * This is very essential or else the database would be
     * overloaded with useless open connections.
     */
    public void close() {
	try {
	    connection.close();
	}
	catch (Exception e) {
	}
    }
}
