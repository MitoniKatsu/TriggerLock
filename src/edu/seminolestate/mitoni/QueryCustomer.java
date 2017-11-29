package edu.seminolestate.mitoni;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the customer query, the back-end logic and methods behind any SQL processes on the customer table
 */
public class QueryCustomer extends Query
{

	public QueryCustomer(String user, String pass, String path)
	{
		super(user, pass, path);
	}

	public void addCustomer(Customer newCust)
	{
		String sqlQuery = "";
		
		// full SQL Insert Statement
		if (newCust.getIsMember() == 1)
		{
			sqlQuery = "INSERT INTO SYSTEM.CUSTOMERS (customer#, firstname, lastname, address, city, state, zip, phone, email, membership, membership_exp)"
					+ " VALUES (system.customer_customer#_seq.nextVal ,'" + newCust.getFirstName() + "', '"
					+ newCust.getLastName() + "', '" + newCust.getAddress() + "', '" + newCust.getCity() + "', '"
					+ newCust.getState().toString() + "'," + newCust.getZipCode() + ", " + newCust.getPhoneNum() + ", '"
					+ newCust.getEmail() + "', " + newCust.getIsMember() + ", TO_DATE('" + newCust.getMemberExp()
					+ "', 'MM/DD/YYYY'))";
		}
		else
		{
			sqlQuery = "INSERT INTO SYSTEM.CUSTOMERS (customer#, firstname, lastname, address, city, state, zip, phone, email, membership, membership_exp)"
					+ " VALUES (system.customer_customer#_seq.nextVal ,'" + newCust.getFirstName() + "', '"
					+ newCust.getLastName() + "', '" + newCust.getAddress() + "', '" + newCust.getCity() + "', '"
					+ newCust.getState().toString() + "'," + newCust.getZipCode() + ", " + newCust.getPhoneNum() + ", '"
					+ newCust.getEmail() + "', " + newCust.getIsMember() + ", null)";
		}
		

		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			stmt.execute(sqlQuery);
			// verify customer number for newly created record
			ResultSet rs = stmt.executeQuery("SELECT system.customer_customer#_seq.currval FROM dual");
			rs.next();
			// pass to customer object
			newCust.setCustNum(rs.getInt(1));
			newConnect.commit();
			JOptionPane.showMessageDialog(null, "Customer number " + newCust.getCustNum() + " created Successfully.");
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null,
					"Customer creation failed. Please try again, or see your system admin.");
			System.out.println(e.toString());
		}
	}

	public ResultSet searchCustomerNumber(int custNumber)
	{

		String custNumberQuery = "";

		if (custNumber == 0)
		{
			custNumberQuery = "SELECT lpad(customer#, 6, '0') \"Customer #\", firstname \"First Name\", lastname \"Last Name\", address \"Address\", "
					+ "city \"City\", state \"State\", zip \"Zip\", phone \"Phone\" FROM system.customers WHERE customer# like '%'";

		}
		else
		{
			custNumberQuery = "SELECT lpad(customer#, 6, '0') \"Customer #\", firstname \"First Name\", lastname \"Last Name\", address \"Address\", "
					+ "city \"City\", state \"State\", zip \"Zip\", phone \"Phone\" FROM system.customers WHERE customer# = "
					+ custNumber;
		}

		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(custNumberQuery);

			return rs;
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Search failed. Please try again, or see your system admin.");
			System.out.println(e.toString());
		}
		return null;
	}

	public ResultSet searchCustomerName(String first, String last)
	{

		String custNameQuery = "";

		custNameQuery = "SELECT lpad(customer#, 6, '0') \"Customer #\", firstname \"First Name\", lastname \"Last Name\", address \"Address\", "
				+ "city \"City\", state \"State\", zip \"Zip\", phone \"Phone\" FROM system.customers WHERE firstname LIKE '"
				+ first.toUpperCase() + "%' " + "AND lastname LIKE '" + last.toUpperCase() + "%'";

		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(custNameQuery);

			return rs;
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Search failed. Please try again, or see your system admin.");
			System.out.println(e.toString());
		}

		return null;
	}

	public ResultSet searchCustomerPhone(long custPhone)
	{

		String custPhoneQuery = "";

		if (custPhone == 0)
		{
			custPhoneQuery = "SELECT lpad(customer#, 6, '0') \"Customer #\", firstname \"First Name\", lastname \"Last Name\", address \"Address\", "
					+ "city \"City\", state \"State\", zip \"Zip\", phone \"Phone\" FROM system.customers WHERE phone like '%'";

		}
		else
		{
			custPhoneQuery = "SELECT lpad(customer#, 6, '0') \"Customer #\", firstname \"First Name\", lastname \"Last Name\", address \"Address\", "
					+ "city \"City\", state \"State\", zip \"Zip\", phone \"Phone\" FROM system.customers WHERE phone = "
					+ custPhone;
		}

		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(custPhoneQuery);

			return rs;
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Search failed. Please try again, or see your system admin.");
			System.out.println(e.toString());
		}

		return null;
	}

	public void deleteCustomer(String rowID)
	{
		String deleteCustQuery = "DELETE FROM system.customers WHERE customer# = " + rowID;
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			stmt.execute(deleteCustQuery);
			newConnect.commit();
			JOptionPane.showMessageDialog(null, "Deleted customer number " + rowID + " successfully.");
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
	
	public ResultSet loadEditCustomer(String custNum)
	{
		String loadCustomerQuery = "SELECT * FROM system.customers WHERE customer# = " + custNum;
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(),
					this.getPassword());

			stmt = newConnect.createStatement();
			ResultSet rs = stmt.executeQuery(loadCustomerQuery);
			newConnect.commit();
			
			return rs;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Unable to load customer record #" + custNum + ". Please try again, or see your system admin.");
		}
		
		return null;
	}
	
	public void updateCustomer(String updateQuery)
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
				JOptionPane.showMessageDialog(null, "Unable to update customer record. Please try again, or see your system admin.");
			}
			finally
			{
				closeSQL();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Unable to update customer record. Please try again, or see your system admin.");
		}
		
	}
	

}
