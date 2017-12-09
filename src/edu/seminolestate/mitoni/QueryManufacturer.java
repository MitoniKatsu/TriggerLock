/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the manufacturer query, the back-end logic and methods behind any SQL processes on the manufacturer table
 */
package edu.seminolestate.mitoni;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class QueryManufacturer extends Query
{

	public QueryManufacturer(String user, String pass, String path)
	{
		super(user, pass, path);
	}
	
	public void addManufacturer(String insertQuery)
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
				JOptionPane.showMessageDialog(null, "Manufacturer added Successfully.");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null,
						"Manufacturer record creation failed. Please try again, or see your system admin.");
				System.out.println(e.toString());
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null,	"Manufacturer record creation failed. Please try again, or see your system admin.");
		}
	}
	
	public ResultSet listManufacturers()
	{
		
		String manfQuery = "SELECT company_name \"Company Name\" from SYSTEM.MANUFACTURER";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(manfQuery);
			
			return rs;
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load Manufacturer List. Please see your system admin.");
		}
		
		return null;

	}
	
	public void deleteManufacturer(String companyName)
	{
		String deleteManfQuery = "DELETE FROM system.manufacturer WHERE company_name = '" + companyName + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			stmt.execute(deleteManfQuery);
			newConnect.commit();
			JOptionPane.showMessageDialog(null, "Deleted Manufacturer" + companyName + " successfully.");
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
	
	public void updateManufacturer(String updateQuery)
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
				JOptionPane.showMessageDialog(null, "Unable to update manufacturer record. Please try again, or see your system admin.");
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Unable to update manufacturer record. Please try again, or see your system admin.");
		}
		
	}
	
	public ResultSet loadEditManufacturer(String companyName)
	{
		String loadManfQuery = "SELECT * FROM system.manufacturer WHERE company_name = '" + companyName + "'";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(loadManfQuery);
			newConnect.commit();
			
			return rs;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load record of manufacturer:" + companyName + ". Please try again, or see your system admin.");
		}
		
		return null;
	}

}
