package edu.seminolestate.mitoni;


import java.sql.DriverManager;
import java.sql.SQLException;


import javax.swing.JOptionPane;

/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the user query logic and methods related to SQL users
 */
public class UserQuery extends Query
{


	public UserQuery(String user, String pass, String path)
	{
		super(user, pass, path);
	}
	
	public void createUser(User newUser)
	{
				
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			
			stmt = newConnect.createStatement();
			stmt.execute("CREATE USER " + newUser.getUsername() + " IDENTIFIED BY " + newUser.getPassword());
			stmt.execute("GRANT " + newUser.getUserRole().toString() + " TO " + newUser.getUsername());
			newConnect.close();
			JOptionPane.showMessageDialog(null, "User " + newUser.getUsername().toUpperCase() + " Created Successfully.");
		} 
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Creation failed. Please try again, or see your system admin.");
			System.out.println(e.toString());
		}
	}
		
	public void dropUser(String user)
	{
				
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			
			stmt = newConnect.createStatement();
			stmt.execute("DROP USER " + user);
			newConnect.close();
			JOptionPane.showMessageDialog(null, "User " + user.toUpperCase() + " Dropped Successfully.");
		} 
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Deletion failed. Please try again, or see your system admin.");
			System.out.println(e.toString());
		}
	}

	public void updateUser(String revokeQuery, String grantQuery)
	{
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			
			stmt = newConnect.createStatement();
			stmt.execute(revokeQuery);
			stmt.execute(grantQuery);
			newConnect.close();
			JOptionPane.showMessageDialog(null, "User role updated Successfully.");
		} 
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Update failed. Please try again, or see your system admin.");
			System.out.println(e.toString());
		}
		finally
		{
			closeSQL();
		}
		
	}
}
