/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the caliber query, the back-end logic and methods behind any SQL processes on the customer table
 */

package edu.seminolestate.mitoni;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class QueryCaliber extends Query
{

	public QueryCaliber(String user, String pass, String path)
	{
		super(user, pass, path);
	}
	
	public void addCaliber(String insertQuery)
	{
		if (insertQuery != "")
		{
			try
			{
				newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
						this.getPassword());

				stmt = newConnect.createStatement();
				stmt.execute(insertQuery);
				newConnect.commit();
				JOptionPane.showMessageDialog(null, "Caliber added Successfully.");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null,"Caliber record creation failed. Please try again, or see your system admin.");
				System.out.println(e.toString());
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null,	"Caliber record creation failed. Please try again, or see your system admin.");
		}
	}

	public void deleteCaliber(String caliber)
	{
		String deleteCaliberQuery = "DELETE FROM system.caliber WHERE caliber = '" + caliber + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			stmt.execute(deleteCaliberQuery);
			newConnect.commit();
			JOptionPane.showMessageDialog(null, "Deleted caliber" + caliber + " successfully.");
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

	public ResultSet listCalibers()
	{
		String caliberQuery = "SELECT caliber \"Caliber\" from SYSTEM.CALIBER";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(caliberQuery);
			
			return rs;
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load Caliber List. Please see your system admin.");
		}
		
		return null;
	}

	public ResultSet loadEditCaliber(String caliber)
	{
		String loadCaliberQuery = "SELECT * FROM system.CALIBER WHERE CALIBER = '" + caliber + "'";
		
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
			JOptionPane.showMessageDialog(null, "Unable to load record of " + caliber + ". Please try again, or see your system admin.");
		}
		
		return null;
	}
	
	public void updateCaliber(String updateQuery)
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
				JOptionPane.showMessageDialog(null, "Unable to update caliber record. Please try again, or see your system admin.");
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Unable to update caliber record. Please try again, or see your system admin.");
		}
	}

}
