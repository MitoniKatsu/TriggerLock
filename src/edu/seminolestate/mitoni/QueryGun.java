/* 
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the gun query, the back-end logic and methods behind any SQL processes on the gun table
 */
package edu.seminolestate.mitoni;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class QueryGun extends Query
{

	public QueryGun(String user, String pass, String path)
	{
		super(user, pass, path);
	}
	
	public void addGun(String insertQuery)
	{
		if (insertQuery != "")
		{
			try
			{
				newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe",
						this.getUsername(), this.getPassword());

				stmt = newConnect.createStatement();
				stmt.execute(insertQuery);
				newConnect.commit();
				JOptionPane.showMessageDialog(null, "New gun record added Successfully.");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null,
						"Gun record creation failed. Please try again, or see your system admin.");
				System.out.println(e.toString());
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null,
					"Gun record creation failed. Please try again, or see your system admin.");
		}
	}
	
	public ResultSet listGuns()
	{
		String listQuery = "SELECT serial \"Serial\", manufacturer \"Manufacturer\", model \"Model Number\", caliber \"Caliber\", location \"Location\", last_visit_used \"Last Visit Used\" from SYSTEM.GUNS";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(listQuery);
			
			return rs;
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load Guns List. Please see your system admin.");
		}
		
		return null;		
	}
	
	public ResultSet loadEditGun(String serial)
	{
		String loadQuery = "SELECT * FROM system.guns WHERE serial = '" + serial + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),	this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(loadQuery);
			newConnect.commit();
			
			return rs;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load record of gun: " + serial + ". Please try again, or see your system admin.");
		}
		
		return null;		
	}
	
	public void updateGun(String updateQuery)
	{
		if (updateQuery != "")
		{
			try
			{
				newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
						this.getPassword());

				stmt = newConnect.createStatement();
				stmt.execute(updateQuery);
				newConnect.commit();
				JOptionPane.showMessageDialog(null, "Update completed successfully.");
			
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Unable to update gun record. Please try again, or see your system admin.");
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Unable to update gun record. Please try again, or see your system admin.");
		}
	}
	
	public void deleteGun(String serial)
	{
		String deleteQuery = "DELETE FROM system.guns WHERE serial = '" + serial + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),	this.getPassword());

			stmt = newConnect.createStatement();
			stmt.execute(deleteQuery);
			newConnect.commit();
			JOptionPane.showMessageDialog(null, "Deleted Gun " + serial + " successfully.");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Delete failed. Please try again, or see your system admin.");
		}
		finally 
		{
			closeSQL();
		}
	}
	
	public void returnGun(String serial, String location)
	{
		String updateQuery = "UPDATE system.guns SET location = '" + location.toUpperCase() + "' WHERE serial = '" + serial.toUpperCase() + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			stmt.execute(updateQuery);
			newConnect.commit();
			
			if (location == "cleaning")
			{
				JOptionPane.showMessageDialog(null, serial.toUpperCase() +  " returned from range, and sent to cleaning area.");
			}
			else
			{
				JOptionPane.showMessageDialog(null, serial.toUpperCase() +  " returned from cleaning area, and sent to range lockup.");
			}	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to update gun record. Please try again, or see your system admin.");
		}
		finally
		{
			closeSQL();
		}
	}
	
	public ResultSet narrowByManufacturer()
	{
		String query = "SELECT UNIQUE manufacturer FROM system.guns WHERE location = 'LOCKUP' ORDER BY manufacturer";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load gun records. Please try again, or see your system admin.");
		}
		
		return rs;
	}
	
	public ResultSet narrowByCaliber(String manufacturer)
	{
		String query = "SELECT UNIQUE caliber FROM system.guns WHERE manufacturer = '" + manufacturer + "' AND location = 'LOCKUP' ORDER BY '" + manufacturer + "'";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load gun records. Please try again, or see your system admin.");
		}
		
		return rs;		
	}
	
	public ResultSet narrowByModel(String manufacturer, String caliber)
	{
		String query = "SELECT UNIQUE model FROM system.guns WHERE manufacturer = '" + manufacturer + "' AND caliber = '" + caliber + "' AND location = 'LOCKUP' ORDER BY '" + manufacturer +"'";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load gun records. Please try again, or see your system admin.");
		}
		
		return rs;
	}
	
	public ResultSet narrowBySerial(String manufacturer, String caliber, String model)
	{
		String query = "SELECT UNIQUE serial FROM system.guns WHERE manufacturer = '" + manufacturer + "' AND caliber = '" + caliber + "' AND model = '" + model + "' AND location = 'LOCKUP' ORDER BY '" + manufacturer +"'";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load gun records. Please try again, or see your system admin.");
		}
		
		return rs;
	}

}
