/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the Add manufacturer Window, and related methods and event handlers
 */

package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class AddManufacturerWindow
{
	protected JInternalFrame frmAddManufacturer;
	
	private JButton btnAddManfSubmit;
	private JButton btnAddManfCancel;
	
	private JFormattedTextField fldCompanyName;
	private JFormattedTextField fldAddress;
	private JFormattedTextField fldCity;
	private JComboBox<String> cmbxState;
	private JFormattedTextField fldZip;
	private JFormattedTextField fldPhone;
	
	// array of us state abbreviations
	private final String[] STATES = { "~~", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI",
		"IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE",
		"NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA",
		"WI", "WV", "WY" };

	public AddManufacturerWindow()
	{
		//build window components
		frmAddManufacturer = new JInternalFrame("Add Manufacturer");
		frmAddManufacturer.setBounds(100, 100, 600, 200);
		frmAddManufacturer.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required fields");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(10,141,93,14);
		frmAddManufacturer.getContentPane().add(lblRequired);
		
		JLabel lblCompanyName = new JLabel("*Company Name");
		lblCompanyName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCompanyName.setBounds(-5,17,131,14);
		frmAddManufacturer.getContentPane().add(lblCompanyName);
		
		fldCompanyName = new JFormattedTextField();
		fldCompanyName.setBounds(136,14,408,20);
		frmAddManufacturer.getContentPane().add(fldCompanyName);
		fldCompanyName.setDocument(new FieldLimit(20));
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddress.setBounds(20,42,66,14);
		frmAddManufacturer.getContentPane().add(lblAddress);
		
		fldAddress = new JFormattedTextField();
		fldAddress.setBounds(113,39,225,20);
		frmAddManufacturer.getContentPane().add(fldAddress);
		fldAddress.setDocument(new FieldLimit(30));
		
		JLabel lblCity = new JLabel("City");
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCity.setBounds(331,42,53,14);
		frmAddManufacturer.getContentPane().add(lblCity);
		
		fldCity = new JFormattedTextField();
		fldCity.setBounds(394,39,150,20);
		frmAddManufacturer.getContentPane().add(fldCity);
		fldCity.setDocument(new FieldLimit(20));
		
		JLabel lblState = new JLabel("State");
		lblState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblState.setBounds(10,67,66,14);
		frmAddManufacturer.getContentPane().add(lblState);
		
		cmbxState = new JComboBox<String>(STATES);
		cmbxState.setBounds(113,64,66,20);
		frmAddManufacturer.getContentPane().add(cmbxState);
		
		JLabel lblZip = new JLabel("Zip code");
		lblZip.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZip.setBounds(172,67,66,14);
		frmAddManufacturer.getContentPane().add(lblZip);
		
		fldZip = new JFormattedTextField();
		fldZip.setBounds(248,64,50,20);
		frmAddManufacturer.getContentPane().add(fldZip);
		fldZip.setDocument(new FieldLimit(5));
		
		JLabel lblPhone = new JLabel("Contact Phone");
		lblPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPhone.setBounds(291,67,93,14);
		frmAddManufacturer.getContentPane().add(lblPhone);
		
		fldPhone = new JFormattedTextField();
		fldPhone.setBounds(394,64,150,20);
		frmAddManufacturer.getContentPane().add(fldPhone);
		fldPhone.setDocument(new FieldLimit(12));
		
		btnAddManfSubmit = new JButton("Submit");
		frmAddManufacturer.getRootPane().setDefaultButton(btnAddManfSubmit);
		btnAddManfSubmit.setBounds(484,137,90,23);
		frmAddManufacturer.getContentPane().add(btnAddManfSubmit);
		
		btnAddManfCancel = new JButton("Cancel");
		btnAddManfCancel.setBounds(384,137,90,23);
		frmAddManufacturer.getContentPane().add(btnAddManfCancel);
		
		//event handlers
		btnAddManfCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				Clear();				
			}
		});
		
		btnAddManfSubmit.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				QueryManufacturer newQuery = new QueryManufacturer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				newQuery.addManufacturer(validateAddManufacturer());
				Clear();
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
	
	public void Clear()
	{
		fldCompanyName.setText("");
		fldAddress.setText("");
		fldCity.setText("");
		fldZip.setText("");
		fldPhone.setText("");
		cmbxState.setSelectedIndex(0);
		frmAddManufacturer.setVisible(false);
	}
	
	public String validateAddManufacturer()
	{
		//validate and builds an insert string
		String insertQuery = "";
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
					insertQuery = "INSERT INTO SYSTEM.MANUFACTURER (company_name, address, city, state, zip, contact_phone)"
					+ " VALUES ('" + companyName + "', '" + address + "', '" + city + "', '" + state + "', '" + strZip + "', " + strPhone +")";
					
				}
				else
				{
					return insertQuery;
				}
				
				
		return insertQuery;
	}

}
