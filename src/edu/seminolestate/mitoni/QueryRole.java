package edu.seminolestate.mitoni;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the role query, used when authenticating login credentials and access restrictions
 */
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
	
	public ResultSet loadEditUserRole(String user)
	{
		String loadCaliberQuery = "SELECT * FROM DBA_role_privs WHERE grantee = '" + user + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(loadCaliberQuery);
			newConnect.commit();
			
			return rs;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load record of " + user + ". Please try again, or see your system admin.");
		}
		
		return null;
	}
	
	public ResultSet listRoles()
	{
		String roleQuery = "SELECT grantee \"Username\", granted_role \"Role\" FROM DBA_role_privs " +
				"where (granted_role = 'DBA' OR " +
				"granted_role = 'OWNER' OR " +
				"granted_role = 'MANAGER' OR " +
				"granted_role = 'RETAIL') AND " +
				"(grantee != 'SYS' AND " +
				"grantee != 'SYSTEM' AND " +
				"grantee != 'SYSADMIN') AND " +
				"grantee != (SELECT USERNAME FROM user_role_privs) " +
				"ORDER BY \"Role\"";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(roleQuery);
			
			return rs;
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load User list. Please see your system admin.");
		}
		
		return null;
		
	}

	
}
