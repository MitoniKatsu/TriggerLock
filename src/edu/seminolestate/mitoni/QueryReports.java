/* 
 * Written by Christian Lundblad
 * December 9, 2017
 * This class contains the reports query, the back-end logic and methods behind any SQL processes on pulling report data
 */
package edu.seminolestate.mitoni;

import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class QueryReports extends Query
{

	public QueryReports(String user, String pass, String path)
	{
		super(user, pass, path);
	}
	
	public ResultSet serviceContactQuery()
	{
		String reportQuery = "SELECT serial \"Serial #\", "
				+ "rpad(manufacturer || ', ' || model, 52, ' ') \"Full Model Name\", "
				+ "rpad(b.address || ', ' || b.city || ', ' || b.state || ' ' || to_char(b.zip, '00000'), 62, ' ') \"Service Address\", "
				+ "to_char(b.contact_phone, '9999999999') \"Service Contact\" "
				+ "FROM system.guns a LEFT JOIN system.manufacturer b "
				+ "ON a.manufacturer = b.company_name "
				+"ORDER BY a.serial";
		
		ResultSet rs = null;
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),	this.getPassword());

			stmt = newConnect.createStatement();
			rs = stmt.executeQuery(reportQuery);					
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "");
		}
		
		return rs;
	}

}
