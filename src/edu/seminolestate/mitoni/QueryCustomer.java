package edu.seminolestate.mitoni;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.swing.JOptionPane;


public class QueryCustomer extends Query
{


	public QueryCustomer(String user, String pass, String path)
	{
		super(user, pass, path);
	}
	
	public void addCustomer(Customer newCust)
	{	
		//full SQL Insert Statement
		String sqlQuery = "INSERT INTO SYSTEM.CUSTOMERS (customer#, firstname, lastname, address, city, state, zip, phone, email, membership, membership_exp)"
			+ " VALUES (system.customer_customer#_seq.nextVal ,'"+ newCust.getFirstName() +"', '"+ newCust.getLastName() +"', '"+ newCust.getAddress() +"', '"
			+ newCust.getCity() +"', '"+ newCust.getState().toString() +"',"+ newCust.getZipCode() +", "+ newCust.getPhoneNum() +", '"+ newCust.getEmail() 
			+"', "+ newCust.getIsMember() +", TO_DATE('"+ newCust.getMemberExp() +"', 'MM/DD/YYYY'))";
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
			
			stmt = newConnect.createStatement();
			stmt.execute(sqlQuery);
			//verify customer number for newly created record
			ResultSet rs = stmt.executeQuery("SELECT system.customer_customer#_seq.currval FROM dual");
			rs.next();
			//pass to customer object
			newCust.setCustNum(rs.getInt(1));
			newConnect.commit();
			JOptionPane.showMessageDialog(null, "Customer number "+ newCust.getCustNum() +" created Successfully.");
		} 
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Customer creation failed. Please try again, or see your system admin.");
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
					+ "city \"City\", state \"State\", zip \"Zip\", phone \"Phone\" FROM system.customers WHERE customer# = " + custNumber;
		}
		
		
		try
		{
			newConnect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.getPath() + ":xe", this.getUsername(), this.getPassword());
						
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
	
	
}
