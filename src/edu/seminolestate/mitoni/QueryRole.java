package edu.seminolestate.mitoni;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;



public class QueryRole extends Query
{


	public enum Role //database role types
	{
		DBA, OWNER, MANAGER, RETAIL, INVALID;
	}
	
	public QueryRole(String user, String password, String path)
	{
		super(user,password,path);
	}
	
	public Role CheckLogin()
	{
		Role role = Role.INVALID;
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + getPath() + ":xe", getUsername(), getPassword());
			
			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT granted_role FROM user_role_privs"); //get current user role
			
			rs.next();
			switch (rs.getString(1))
			{
			case "DBA":
				role = Role.DBA;
				break;
			case "OWNER":
				role = Role.OWNER;
				break;
			case "MANAGER":
				role = Role.MANAGER;
				break;
			case "RETAIL":
				role = Role.RETAIL;
				break;
			default:
				break;
			}
			
			stmt.close();
			newConnect.close();
		} 
		catch (SQLException e)
		{
			System.out.println(e.toString());
			role =  Role.INVALID;
		}
		
		return role;
	}

	
}
