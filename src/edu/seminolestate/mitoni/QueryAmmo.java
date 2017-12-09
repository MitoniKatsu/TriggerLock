/* 
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the ammo query, the back-end logic and methods behind any SQL processes on the ammo table
 */
package edu.seminolestate.mitoni;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class QueryAmmo extends Query
{

	public QueryAmmo(String user, String pass, String path)
	{
		super(user, pass, path);
	}

	public void addAmmo(String insertQuery)
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
				JOptionPane.showMessageDialog(null, "Ammo added Successfully.");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null,
						"Ammo record creation failed. Please try again, or see your system admin.");
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
					"Ammo record creation failed. Please try again, or see your system admin.");
		}
	}
	
	public ResultSet listAmmo()
	{
		
		String listQuery = "SELECT manufacturer \"Manufacturer\", model \"Model Number\", caliber \"Caliber\", qty_stocked \"Qty in Stock\", to_char(cost_per_round, '$99.99') \"Cost per Round\" from SYSTEM.AMMO";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(listQuery);
			
			return rs;
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load Ammo List. Please see your system admin.");
		}
		
		return null;

	}
	
	public void deleteAmmo(String model)
	{
		String deleteQuery = "DELETE FROM system.ammo WHERE model = '" + model + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			stmt.execute(deleteQuery);
			newConnect.commit();
			JOptionPane.showMessageDialog(null, "Deleted Ammo " + model + " successfully.");
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
	
	public void updateAmmo(String updateQuery)
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
				JOptionPane.showMessageDialog(null, "Unable to update ammo record. Please try again, or see your system admin.");
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Unable to update ammo record. Please try again, or see your system admin.");
		}
		
	}
	
	public ResultSet loadEditAmmo(String model)
	{
		String loadQuery = "SELECT * FROM system.ammo WHERE model = '" + model + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(loadQuery);
			newConnect.commit();
			
			return rs;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load record of ammo: " + model + ". Please try again, or see your system admin.");
		}
		
		return null;
	}

	public ResultSet narrowByCaliber()
	{
		String query = "SELECT UNIQUE caliber FROM system.ammo WHERE qty_stocked >= 10";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load ammo records. Please try again, or see your system admin.");
		}
		
		return rs;
	}
	
	public ResultSet narrowByManufacturer(String caliber)
	{
		String query = "SELECT UNIQUE manufacturer FROM system.ammo WHERE qty_stocked >= 10 AND caliber = '" + caliber + "'";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load ammo records. Please try again, or see your system admin.");
		}
		
		return rs;		
	}
	
	public ResultSet narrowByModel(String caliber, String manufacturer)
	{
		String query = "SELECT UNIQUE model, qty_stocked, cost_per_round FROM system.ammo WHERE caliber = '" + caliber + "' AND manufacturer = '" + manufacturer + "' AND qty_stocked > 9";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load ammo records. Please try again, or see your system admin.");
		}
		
		return rs;
	}
	
	public ResultSet narrowByQtyStocked(String caliber, String manufacturer, String model)
	{
		String query = "SELECT qty_stocked, model, cost_per_round FROM system.ammo WHERE caliber = '" + caliber + "' AND manufacturer = '" + manufacturer + "' AND model = '" + model + "' AND qty_stocked > 9";
		ResultSet rs = null;
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(query);	
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load ammo records. Please try again, or see your system admin.");
		}
		
		return rs;
	}

}
