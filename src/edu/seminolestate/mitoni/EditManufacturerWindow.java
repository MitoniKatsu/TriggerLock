/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the Edit Manufacturer Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class EditManufacturerWindow
{
	protected JInternalFrame frmEditManufacturer;
	private JButton btnEditManufacturerSave;
	private JButton btnEditManufacturerCancel;
	
	private JFormattedTextField fldCompanyName;
	private JFormattedTextField fldAddress;
	private JFormattedTextField fldCity;
	private JComboBox<String> cmbxState;
	private JFormattedTextField fldZip;
	private JFormattedTextField fldPhone;
	private String companyName ="";
	
	// array of us state abbreviations
	private final String[] STATES = { "~~", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI",
		"IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE",
		"NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA",
		"WI", "WV", "WY" };

	public EditManufacturerWindow()
	{
		//build window components
		frmEditManufacturer = new JInternalFrame("Edit Manufacturer");
		frmEditManufacturer.setBounds(100, 100, 600, 200);
		frmEditManufacturer.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required fields");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(10,141,93,14);
		frmEditManufacturer.getContentPane().add(lblRequired);
		
		JLabel lblCompanyName = new JLabel("*Company Name");
		lblCompanyName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCompanyName.setBounds(10,17,93,14);
		frmEditManufacturer.getContentPane().add(lblCompanyName);
		
		fldCompanyName = new JFormattedTextField();
		fldCompanyName.setBounds(113,14,431,20);
		frmEditManufacturer.getContentPane().add(fldCompanyName);
		fldCompanyName.setDocument(new FieldLimit(20));
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddress.setBounds(20,42,66,14);
		frmEditManufacturer.getContentPane().add(lblAddress);
		
		fldAddress = new JFormattedTextField();
		fldAddress.setBounds(113,39,225,20);
		frmEditManufacturer.getContentPane().add(fldAddress);
		fldAddress.setDocument(new FieldLimit(30));
		
		JLabel lblCity = new JLabel("City");
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCity.setBounds(331,42,53,14);
		frmEditManufacturer.getContentPane().add(lblCity);
		
		fldCity = new JFormattedTextField();
		fldCity.setBounds(394,39,150,20);
		frmEditManufacturer.getContentPane().add(fldCity);
		fldCity.setDocument(new FieldLimit(20));
		
		JLabel lblState = new JLabel("State");
		lblState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblState.setBounds(10,67,66,14);
		frmEditManufacturer.getContentPane().add(lblState);
		
		cmbxState = new JComboBox<String>(STATES);
		cmbxState.setBounds(113,64,66,20);
		frmEditManufacturer.getContentPane().add(cmbxState);
		
		JLabel lblZip = new JLabel("Zip code");
		lblZip.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZip.setBounds(172,67,66,14);
		frmEditManufacturer.getContentPane().add(lblZip);
		
		fldZip = new JFormattedTextField();
		fldZip.setBounds(248,64,50,20);
		frmEditManufacturer.getContentPane().add(fldZip);
		fldZip.setDocument(new FieldLimit(5));
		
		JLabel lblPhone = new JLabel("Contact Phone");
		lblPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPhone.setBounds(291,67,93,14);
		frmEditManufacturer.getContentPane().add(lblPhone);
		
		fldPhone = new JFormattedTextField();
		fldPhone.setBounds(394,64,150,20);
		frmEditManufacturer.getContentPane().add(fldPhone);
		fldPhone.setDocument(new FieldLimit(12));
		
		btnEditManufacturerSave = new JButton("Save Changes");
		frmEditManufacturer.getRootPane().setDefaultButton(btnEditManufacturerSave);
		btnEditManufacturerSave.setBounds(424,137,150,23);
		frmEditManufacturer.getContentPane().add(btnEditManufacturerSave);
		
		btnEditManufacturerCancel = new JButton("Cancel");
		btnEditManufacturerCancel.setBounds(324,137,90,23);
		frmEditManufacturer.getContentPane().add(btnEditManufacturerCancel);
		
		//Event Handlers
		btnEditManufacturerCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				clear();			
			}
		});
		
		btnEditManufacturerSave.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				QueryManufacturer newQuery = new QueryManufacturer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				newQuery.addManufacturer(validateEditManufacturer());
				clear();
			}
		});
	}
	
	//getters and setters
		
	public String getFldCompanyName()
	{
		return fldCompanyName.getText();
	}
	
	public String getFldAddress()
	{
		return fldAddress.getText();
	}
	
	public String getFldCity()
	{
		return fldCity.getText();
	}
	
	public String getFldPhone()
	{
		return fldPhone.getText();
	}
	
	public String getFldZip()
	{
		return fldZip.getText();
	}
	
	public int getCmbxState()
	{
		return cmbxState.getSelectedIndex();
	}
	
	//public methods
	
	public void clear()
	{
		fldCompanyName.setText("");
		fldAddress.setText("");
		fldCity.setText("");
		fldPhone.setText("");
		cmbxState.setSelectedIndex(0);
		frmEditManufacturer.setVisible(false);
	}
	
	public void parseLoadManufacturer(ResultSet newLoad)
	{
		
		try
		{
			newLoad.next();
			//company name
			companyName = newLoad.getString(1);
			fldCompanyName.setText(companyName);
			//contact phone
			String phone = newLoad.getString(2);
			fldPhone.setText(phone.substring(0,3) + "-" + phone.substring(3,6) + "-" + phone.substring(6,10));
			//address
			fldAddress.setText(newLoad.getString(3));
			//city
			fldCity.setText(newLoad.getString(4));
			//find index of state
			int index = 0;
			
			for (int i = 0; i < STATES.length; i++)
			{
				if (newLoad.getString(5).charAt(0) == STATES[i].charAt(0))
				{
					if (newLoad.getString(5).charAt(1) == STATES[i].charAt(1))
					{
						index = i;
					}
				}		
			}
			cmbxState.setSelectedIndex(index);
			//zip
			fldZip.setText(newLoad.getString(6));		
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to parse result. Please try again, or see your system admin.");
			System.out.println(e.toString());			
		}
	}
	
	public String validateEditManufacturer()
	{
		//validate and builds an insert string
		String updateQuery = "";
		String companyName = "";
		String address = "";
		String city = "";
		String state = "";
		String strZip = "";
		String strPhone = "";
		int zip = 0;
		long phone = (long) 0;
		
		boolean valid = true;
		
		// validate company name
		if (fldCompanyName.getText().trim().isEmpty())
		{
			valid = false;
			JOptionPane.showMessageDialog(null, "A valid company name is required.");
		}
		else
		{
			// pass address
			companyName = fldCompanyName.getText().toUpperCase();
			
			// pass address
			address = fldAddress.getText().toUpperCase();

			// pass city
			city = fldCity.getText().toUpperCase();

			// validate State
			if (cmbxState.getSelectedIndex() == 0)
			{
				state = "";
			}
			else
			{
				// pass State
				state = STATES[cmbxState.getSelectedIndex()].toUpperCase();
			}
		}
		
		// validate zip
		if (valid && !fldZip.getText().trim().isEmpty())
		{
			try
			{
				zip = Integer.parseInt(fldZip.getText());
				strZip = Integer.toString(zip);
			}
			catch (NumberFormatException e)
			{
				valid = false;
				JOptionPane.showMessageDialog(null,
						"Manufacturer record creation failed. Zip code must be a valid entry, or blank.");
			}
		}
		
		// validate phone
		if (valid && !fldPhone.getText().trim().isEmpty())
		{
			Pattern p = Pattern.compile("(\\d{3}) ?-?(\\d{3}) ?-?(\\d{4})");
			Matcher m = p.matcher(fldPhone.getText());
			if (m.matches())
			{
				String longString = m.group(1) + m.group(2) + m.group(3);
				// parse string to an integer
				try
				{
					phone = Long.parseLong(longString);
					strPhone = Long.toString(phone);
				}
				catch (NumberFormatException n)
				{
					valid = false;
					JOptionPane.showMessageDialog(null,
							"Manufacturer record creation failed. Phone number should only include numbers, dashes, or spaces ie: 555 555-5555.");
				}
			}
			else
			{
				valid = false;
				JOptionPane.showMessageDialog(null,
						"Manufacturer record creation failed. Phone number should only include numbers, dashes, or spaces ie: 555 555-5555.");
			}
		}
		
		if (valid)
		{
			updateQuery = "UPDATE system.manufacturer "
					+ "SET COMPANY_NAME = '" + companyName
					+ "', ADDRESS = '" + address
					+ "', CITY = '" + city
					+ "', STATE = '" + state
					+ "', ZIP = " + strZip
					+ ", CONTACT_PHONE = " + strPhone
					+ " WHERE " 
					+ "COMPANY_NAME = '" + companyName + "'";					
		}
		else
		{
			return updateQuery;
		}
				
				
		return updateQuery;
	}
	
}
