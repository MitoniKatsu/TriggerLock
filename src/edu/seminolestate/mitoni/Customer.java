package edu.seminolestate.mitoni;

import java.sql.Date;
import java.text.SimpleDateFormat;

/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the customer object, to be passed to customer queries
 */
public class Customer
{
	
	//field variables
	private int custNum;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private int zipCode;
	private Long phoneNum;
	private String email;
	private boolean memberYN;
	private java.util.Date memberExp;
	
	//Constructor with all arguments required
	public Customer(int custNum, String first, String last, String address, String city, String state, int zip, long phone, String email, boolean member, java.util.Date memExp)
	{
		this.custNum = custNum;
		this.firstName = first;
		this.lastName = last;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zip;
		this.phoneNum = phone;
		this.email = email;
		this.memberYN = member;
		this.memberExp = memExp;
	}
	
	//public get/set
	public int getCustNum()
	{
		return custNum;
	}
	
	public void setCustNum(int custNum)
	{
		this.custNum = custNum;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public int getZipCode()
	{
		return zipCode;
	}

	public void setZipCode(int zipCode)
	{
		this.zipCode = zipCode;
	}

	public long getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(long phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public int getIsMember()
	{
		if (memberYN)
		{
			return 1;
		}
		else 
		{
			return 0;
		}
	}

	public void setMember(boolean memberYN)
	{
		this.memberYN = memberYN;
	}

	public String getMemberExp()
	{
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		String expDate = format.format(memberExp);
		
		return expDate;
	}

	public void setMemberExp(Date memberExp)
	{
		this.memberExp = memberExp;
	}
	
	
	
	
}
