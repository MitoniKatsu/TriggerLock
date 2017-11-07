package edu.seminolestate.mitoni;


import java.sql.DriverManager;
import java.sql.SQLException;


import javax.swing.JOptionPane;


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
}
