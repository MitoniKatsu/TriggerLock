package edu.seminolestate.mitoni;

import edu.seminolestate.mitoni.QueryRole.Role;
/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the user object
 */
public class User
{
	private String username;
	private String password;
	private Role userRole;
	
	public User(String user, String pass, Role role)
	{
		this.username = user;
		this.password = pass;
		this.userRole = role;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public Role getUserRole()
	{
		return userRole;
	}
	
}
