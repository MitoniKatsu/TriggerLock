/* 
 * Written by Christian Lundblad
 * December 9, 2017
 * This class contains the range visit query, the back-end logic and methods behind any SQL processes on the range visit table
 */
package edu.seminolestate.mitoni;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

public class QueryRangeVisit extends Query
{

	public QueryRangeVisit(String user, String pass, String path)
	{
		super(user, pass, path);
	}
	
	public void addRangeVisit(String custNum, double cost, String serial, String ammo, int ammoNewQty, boolean renew, Date renewFrom, boolean start)
	{
		java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
		int visit = 0;
		
		String visitQuery = "INSERT INTO system.range_visit (id#, date_of_visit, trip_cost) "
				+ "values (system.range_visit_id#_seq.nextVal, TO_DATE('" + today
					+ "', 'YYYY-MM-DD'), " + cost + ")";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			//add visit to range_visit table
			stmt.execute(visitQuery);
			// verify visit number for newly created record
			ResultSet rs = stmt.executeQuery("SELECT system.range_visit_id#_seq.currval FROM dual");
			rs.next();
			// pass to visit
			visit = rs.getInt(1);
			//pass new visit to customer record
			stmt.execute("UPDATE system.customers SET last_visit = " + visit + " WHERE customer# =" + custNum);
			//if rental, pass visit id to gun and serial to customer
			if (serial != "")
			{
				//pass serial to customer last_gun_used
				stmt.execute("UPDATE system.customers SET last_gun_used = '" + serial + "' WHERE customer# =" + custNum);
				//change location of gun to range-custnum
				stmt.execute("UPDATE system.guns SET location = 'RANGE-' || lpad(" + custNum + ", 6, '0') WHERE serial ='" + serial + "'");
				JOptionPane.showMessageDialog(null, serial + " checked out to range");
				stmt.execute("UPDATE system.guns SET last_visit_used = " + visit + " WHERE serial = '" + serial + "'");
			}
			//if ammo purchase, update qty in stock
			if (ammo != "")
			{
				//pass newQty to ammo
				stmt.execute("UPDATE system.ammo SET qty_stocked = " + ammoNewQty + " WHERE model = '" + ammo + "'");
			}
			//if starting new membership
			if (start)
			{
				stmt.execute("UPDATE system.customers SET membership = 1, membership_exp = ADD_MONTHS(TO_DATE('" + today + "', 'YYYY-MM-DD'), 12) WHERE customer# = " + custNum);
			}
			// if renewing membership
			if (renew)
			{
				stmt.execute("UPDATE system.customers SET membership_exp = ADD_MONTHS(TO_DATE('" + renewFrom + "', 'YYYY-MM-DD'), 12) WHERE customer# = " + custNum);
			}
			newConnect.commit();		
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to add range visit. Please try again, or see your system admin.");
		}
		finally
		{
			closeSQL();
		}		
	}

}
