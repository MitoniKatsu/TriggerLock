/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the add customer window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class AddCustomerWindow
{
	protected JInternalFrame frmAddCustomer;
	private JButton btnAddCustSubmit;
	private JButton btnAddCustCancel;
	
	private JFormattedTextField fldAddress;
	private JComboBox<String> cmbxState;
	private JFormattedTextField fldZip;
	private JFormattedTextField fldCity;
	private JFormattedTextField fldEmail;
	private JCheckBox ckbxMember;
	private JFormattedTextField fldLast;
	private JFormattedTextField fldFirst;
	private JFormattedTextField fldPhone;
	
	// array of us state abbreviations
	private final String[] STATES = { "~~", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI",
				"IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE",
				"NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA",
				"WI", "WV", "WY" };
	
	public AddCustomerWindow()
	{
		//build window components
		frmAddCustomer = new JInternalFrame("Add Customer");
		frmAddCustomer.setBounds(147, 477, 600, 200);
		frmAddCustomer.getContentPane().setLayout(null);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddress.setBounds(10, 39, 70, 14);
		frmAddCustomer.getContentPane().add(lblAddress);

		fldAddress = new JFormattedTextField();
		fldAddress.setBounds(90, 36, 225, 20);
		frmAddCustomer.getContentPane().add(fldAddress);
		fldAddress.setDocument(new FieldLimit(30));

		JLabel lblState = new JLabel("State");
		lblState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblState.setBounds(20, 64, 60, 14);
		frmAddCustomer.getContentPane().add(lblState);

		cmbxState = new JComboBox<String>();
		cmbxState.setBounds(88, 61, 66, 20);
		frmAddCustomer.getContentPane().add(cmbxState);
		loadStates();

		JLabel lblZip = new JLabel("Zip Code");
		lblZip.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZip.setBounds(150, 64, 64, 14);
		frmAddCustomer.getContentPane().add(lblZip);

		fldZip = new JFormattedTextField();
		fldZip.setBounds(230, 61, 50, 20);
		frmAddCustomer.getContentPane().add(fldZip);
		fldZip.setDocument(new FieldLimit(5));

		JLabel lblCity = new JLabel("City");
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCity.setBounds(324, 39, 39, 14);
		frmAddCustomer.getContentPane().add(lblCity);

		JLabel lblPhone = new JLabel("Phone (with area code)");
		lblPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPhone.setBounds(290, 64, 137, 14);
		frmAddCustomer.getContentPane().add(lblPhone);

		fldCity = new JFormattedTextField();
		fldCity.setBounds(370, 36, 150, 20);
		frmAddCustomer.getContentPane().add(fldCity);
		fldCity.setDocument(new FieldLimit(20));

		fldPhone = new JFormattedTextField();
		fldPhone.setBounds(437, 61, 83, 20);
		frmAddCustomer.getContentPane().add(fldPhone);
		fldPhone.setDocument(new FieldLimit(12));

		fldFirst = new JFormattedTextField();
		fldFirst.setBounds(98, 11, 90, 20);
		frmAddCustomer.getContentPane().add(fldFirst);
		fldFirst.setDocument(new FieldLimit(12));

		fldLast = new JFormattedTextField();
		fldLast.setBounds(266, 11, 90, 20);
		frmAddCustomer.getContentPane().add(fldLast);
		fldLast.setDocument(new FieldLimit(12));

		JLabel lblLast = new JLabel("*Last Name");
		lblLast.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLast.setBounds(186, 14, 70, 14);
		frmAddCustomer.getContentPane().add(lblLast);

		JLabel lblFirst = new JLabel("*First Name");
		lblFirst.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFirst.setBounds(10, 14, 78, 14);
		frmAddCustomer.getContentPane().add(lblFirst);

		ckbxMember = new JCheckBox("Sign Up For Annual Range Membership");
		ckbxMember.setBounds(57, 88, 258, 23);
		frmAddCustomer.getContentPane().add(ckbxMember);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setBounds(348, 14, 46, 14);
		frmAddCustomer.getContentPane().add(lblEmail);

		fldEmail = new JFormattedTextField();
		fldEmail.setBounds(404, 11, 170, 20);
		frmAddCustomer.getContentPane().add(fldEmail);
		fldEmail.setDocument(new FieldLimit(30));

		JLabel lblRequired = new JLabel("* = Required Fields");
		lblRequired.setBounds(404, 92, 116, 14);
		frmAddCustomer.getContentPane().add(lblRequired);
		
		btnAddCustSubmit = new JButton("Submit");
		frmAddCustomer.getRootPane().setDefaultButton(btnAddCustSubmit);
		btnAddCustSubmit.setBounds(40, 137, 89, 23);
		frmAddCustomer.getContentPane().add(btnAddCustSubmit);

		btnAddCustCancel = new JButton("Cancel");
		btnAddCustCancel.setBounds(406, 137, 89, 23);
		frmAddCustomer.getContentPane().add(btnAddCustCancel);
		
		//Event Handlers
		btnAddCustSubmit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				QueryCustomer addNew = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				if (validateAddCustomer() != null)
				{
					addNew.addCustomer(validateAddCustomer());
					clearAddCustomer();
				}
			}
		});

		btnAddCustCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clearAddCustomer();
			}
		});
		
	}
	
	//Getters-Setters
	
	public String getFldFirst()
	{
		return fldFirst.getText();
	}
	
	public String getFldLast()
	{
		return fldLast.getText();
	}
	
	public String getFldAddress()
	{
		return fldAddress.getText();
	}
	
	public String getFldCity()
	{
		return fldCity.getText();
	}
	
	public int getCmbxState()
	{
		return cmbxState.getSelectedIndex();
	}
	
	public String getFldZip()
	{
		return fldZip.getText();
	}
	
	public String getFldPhone()
	{
		return fldPhone.getText();
	}
	
	public boolean getMemberStatus()
	{
		return ckbxMember.isSelected();
	}
	
	//public methods
	
	public void clearAddCustomer()
	{
		fldFirst.setText(null);
		fldLast.setText(null);
		fldAddress.setText(null);
		fldCity.setText(null);
		cmbxState.setSelectedIndex(0);
		fldZip.setText(null);
		fldEmail.setText(null);
		fldPhone.setText(null);
		ckbxMember.setSelected(false);
		frmAddCustomer.setVisible(false);
	}
	
	public Customer validateAddCustomer()
	{
		// validates and builds a customer objects from user input
		String first = "";
		String last = "";
		String address = "";
		String city = "";
		String state = "";
		int zip = 00000;
		long phone = (long) 0000000000;
		String email = "";
		boolean membership = false;
		java.util.Date expDate = null;
		boolean valid = true;

		// validate first name
		if (fldFirst.getText().trim().isEmpty())
		{
			valid = false;
			JOptionPane.showMessageDialog(null, "Customer creation failed. A valid first name is required.");
		}
		else
		{
			// pass first name
			first = fldFirst.getText().toUpperCase();

			// validate last name
			if (fldLast.getText().trim().isEmpty())
			{
				valid = false;
				JOptionPane.showMessageDialog(null, "Customer creation failed. A valid last name is required.");
			}
			else
			{
				// pass last name
				last = fldLast.getText().toUpperCase();

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
		}

		// validate zip
		if (valid && !fldZip.getText().trim().isEmpty())
		{
			try
			{
				zip = Integer.parseInt(fldZip.getText());
			}
			catch (NumberFormatException e)
			{
				valid = false;
				JOptionPane.showMessageDialog(null,
						"Customer creation failed. Zip code must be a valid entry, or blank.");
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
				}
				catch (NumberFormatException n)
				{
					valid = false;
					JOptionPane.showMessageDialog(null,
							"Customer creation failed. Phone number should only include numbers, dashes, or spaces ie: 555 555-5555.");
				}
			}
			else
			{
				valid = false;
				JOptionPane.showMessageDialog(null,
						"Customer creation failed. Phone number should only include numbers, dashes, or spaces ie: 555 555-5555.");
			}
		}

		// validate email
		if (valid && !fldEmail.getText().trim().isEmpty())
		{
			Pattern q = Pattern.compile(
					"^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
			Matcher n = q.matcher(fldEmail.getText());
			if (n.matches())
			{
				email = fldEmail.getText().toUpperCase();
			}
			else
			{
				valid = false;
				JOptionPane.showMessageDialog(null, "Customer creation failed. Email must be a valid entry, or blank.");
			}
		}

		// validate is customer is a member of the range
		if (ckbxMember.isSelected())
		{
			membership = true;
			// set expiration date after 1 year from current
			// date
			Calendar exp = Calendar.getInstance();
			exp.add(Calendar.YEAR, 1);
			java.util.Date nextYear = new java.util.Date();
			nextYear.setTime(exp.getTimeInMillis());
			expDate = nextYear;
		}
		else
		{
			expDate = new Date();
		}

		if (valid)
		{
			Customer newCustomer = new Customer(0, first, last, address, city, state, zip, phone, email, membership,
					expDate);
			return newCustomer;
		}
		else
		{
			return null;
		}
	}
	
	private void loadStates()
	{
		for (int i = 0; i < STATES.length; i++)
		{
			cmbxState.addItem(STATES[i]);
		}
	}
}
