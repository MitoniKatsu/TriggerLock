/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This abstract class contains the query class, extended to all of the different SQL query child classes
 */
package edu.seminolestate.mitoni;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public abstract class Query
{

	private String username = "";		//username for database access
	private String password = "";		//password for database access
	private String dbPath = "localhost:1521";	//path of database
	protected static Statement stmt = null;
	protected static Connection newConnect = null;
	
	public Query(String user, String pass, String path)
	{
		this.username = user;
		this.password = pass;
		if(path != null)
		{
			this.dbPath = path;
		}
	}

	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getPath()
	{
		return dbPath;
	}
	
	//Public methods
	public void closeSQL()
	{
		try
		{
			stmt.close();
			newConnect.close();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error closing SQL resources. See your system administrator.");
		}
	}

}


