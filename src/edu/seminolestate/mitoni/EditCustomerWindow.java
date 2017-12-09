/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the Edit Customer Window, and related methods and event handlers
 */

package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class EditCustomerWindow
{
	protected JInternalFrame frmEditCustomer;
	private JButton btnEditCustCancel;
	private JButton btnEditCustSave;
	
	private JFormattedTextField fldAddress;
	private JComboBox<String> cmbxState;
	private JFormattedTextField fldZip;
	private JFormattedTextField fldCity;
	private JFormattedTextField fldEmail;
	private JCheckBox ckbxMember;
	private JFormattedTextField fldLast;
	private JFormattedTextField fldFirst;
	private JFormattedTextField fldPhone;
	private int custNum = 0;
	
	// array of us state abbreviations
	private final String[] STATES = { "~~", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI",
				"IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE",
				"NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA",
				"WI", "WV", "WY" };
	
	public EditCustomerWindow()
	{
		//build window components
		frmEditCustomer = new JInternalFrame("Edit Customer #" + custNum);
		frmEditCustomer.setBounds(100, 100, 600, 200);
		frmEditCustomer.getContentPane().setLayout(null);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddress.setBounds(10, 39, 70, 14);
		frmEditCustomer.getContentPane().add(lblAddress);

		fldAddress = new JFormattedTextField();
		fldAddress.setBounds(90, 36, 225, 20);
		frmEditCustomer.getContentPane().add(fldAddress);
		fldAddress.setDocument(new FieldLimit(30));

		JLabel lblState = new JLabel("State");
		lblState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblState.setBounds(20, 64, 60, 14);
		frmEditCustomer.getContentPane().add(lblState);

		cmbxState = new JComboBox<String>(STATES);
		cmbxState.setBounds(88, 61, 66, 20);
		frmEditCustomer.getContentPane().add(cmbxState);

		JLabel lblZip = new JLabel("Zip Code");
		lblZip.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZip.setBounds(150, 64, 64, 14);
		frmEditCustomer.getContentPane().add(lblZip);

		fldZip = new JFormattedTextField();
		fldZip.setBounds(230, 61, 50, 20);
		frmEditCustomer.getContentPane().add(fldZip);
		fldZip.setDocument(new FieldLimit(5));

		JLabel lblCity = new JLabel("City");
		lblCity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCity.setBounds(324, 39, 39, 14);
		frmEditCustomer.getContentPane().add(lblCity);

		JLabel lblPhone = new JLabel("Phone (with area code)");
		lblPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPhone.setBounds(290, 64, 137, 14);
		frmEditCustomer.getContentPane().add(lblPhone);

		fldCity = new JFormattedTextField();
		fldCity.setBounds(370, 36, 150, 20);
		frmEditCustomer.getContentPane().add(fldCity);
		fldCity.setDocument(new FieldLimit(20));

		fldPhone = new JFormattedTextField();
		fldPhone.setBounds(437, 61, 83, 20);
		frmEditCustomer.getContentPane().add(fldPhone);
		fldPhone.setDocument(new FieldLimit(12));

		fldFirst = new JFormattedTextField();
		fldFirst.setBounds(90, 11, 90, 20);
		frmEditCustomer.getContentPane().add(fldFirst);
		fldFirst.setDocument(new FieldLimit(12));

		fldLast = new JFormattedTextField();
		fldLast.setBounds(266, 11, 90, 20);
		frmEditCustomer.getContentPane().add(fldLast);
		fldLast.setDocument(new FieldLimit(12));

		JLabel lblLast = new JLabel("*Last Name");
		lblLast.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLast.setBounds(186, 14, 70, 14);
		frmEditCustomer.getContentPane().add(lblLast);

		JLabel lblFirst = new JLabel("*First Name");
		lblFirst.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFirst.setBounds(10, 14, 70, 14);
		frmEditCustomer.getContentPane().add(lblFirst);

		ckbxMember = new JCheckBox("Sign Up For Annual Range Membership");
		ckbxMember.setBounds(57, 88, 258, 23);
		frmEditCustomer.getContentPane().add(ckbxMember);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setBounds(348, 14, 46, 14);
		frmEditCustomer.getContentPane().add(lblEmail);

		fldEmail = new JFormattedTextField();
		fldEmail.setBounds(404, 11, 170, 20);
		frmEditCustomer.getContentPane().add(fldEmail);
		fldEmail.setDocument(new FieldLimit(30));

		JLabel lblRequired = new JLabel("* = Required Fields");
		lblRequired.setBounds(404, 92, 116, 14);
		frmEditCustomer.getContentPane().add(lblRequired);
		
		btnEditCustSave = new JButton("Save Changes");
		frmEditCustomer.getRootPane().setDefaultButton(btnEditCustSave);
		btnEditCustSave.setBounds(437, 137, 137, 23);
		frmEditCustomer.getContentPane().add(btnEditCustSave);

		btnEditCustCancel = new JButton("Cancel");
		btnEditCustCancel.setBounds(338, 137, 89, 23);
		frmEditCustomer.getContentPane().add(btnEditCustCancel);
		
		//event handlers
		btnEditCustCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				clearEditCustomer();
			}
		});
		
		btnEditCustSave.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to update customer number "+ custNum
						+"? This operation cannot be reversed!", "Confirm Update", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION)
				{
					QueryCustomer newQuery = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
					newQuery.updateCustomer(validateEditCustomer());
					clearEditCustomer();
				}
				else
				{
					// Do nothing
				}
				
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
	public void clearEditCustomer()
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
		frmEditCustomer.setVisible(false);
	}
	
	public void parseLoadCustomer(ResultSet newLoad)
	{
		
		try
		{
			newLoad.next();
			//customer number
			custNum = newLoad.getInt(1);
			//last name
			fldLast.setText(newLoad.getString(2));
			//first name
			fldFirst.setText(newLoad.getString(3));
			//address
			fldAddress.setText(newLoad.getString(4));
			//city
			fldCity.setText(newLoad.getString(5));
			//find index of state
			int index = 0;
			
			for (int i = 0; i < STATES.length; i++)
			{
				if (newLoad.getString(6).charAt(0) == STATES[i].charAt(0))
				{
					if (newLoad.getString(6).charAt(1) == STATES[i].charAt(1))
					{
						index = i;
					}
				}		
			}
			cmbxState.setSelectedIndex(index);
			//zip
			fldZip.setText(newLoad.getString(7));
			//phone
			String phone = newLoad.getString(8);
			fldPhone.setText(phone.substring(0,3) + "-" + phone.substring(3,6) + "-" + phone.substring(6,10));
			//email
			fldEmail.setText( newLoad.getString(9));
			//membership
			int bool = (int) newLoad.getInt(10);
			
			if (bool == 1)
			{
				ckbxMember.setSelected(true);
			}
			else
			{
				ckbxMember.setSelected(false);
			}		
			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to parse result. Please try again, or see your system admin.");
			System.out.println(e.toString());
			
		}
	}
	
	public String validateEditCustomer()
	{
		// validates and builds an update String from user input
		String updateQuery = "";
		String first = "";
		String last = "";
		String address = "";
		String city = "";
		String state = "";
		String strZip = "";
		String strPhone = "";
		int zip = 0;
		long phone = (long) 0;
		String email = "";
		int membership = 0;
		boolean valid = true;

		// validate first name
		if (fldFirst.getText().trim().isEmpty())
		{
			valid = false;
			JOptionPane.showMessageDialog(null, "Customer update failed. A valid first name is required.");
		}
		else
		{
			// pass first name
			first = fldFirst.getText().toUpperCase();

			// validate last name
			if (fldLast.getText().trim().isEmpty())
			{
				valid = false;
				JOptionPane.showMessageDialog(null, "Customer update failed. A valid last name is required.");
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
				strZip = Integer.toString(zip);
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
					strPhone = Long.toString(phone);
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
			membership = 1;
		}

		if (valid)
		{
			updateQuery = "UPDATE system.customers "
				+ "SET LASTNAME = '" + last
				+ "',FIRSTNAME = '" + first
				+ "', ADDRESS = '" + address
				+ "', CITY = '" + city
				+ "',STATE = '" + state
				+ "',ZIP = " + strZip
				+ ",PHONE = " + strPhone
				+ ",EMAIL = '" + email
				+ "',MEMBERSHIP = " + membership
				+ " WHERE " 
				+ "customer# = " + custNum;
			
		}
		else
		{
			return updateQuery;
		}
		
		return updateQuery;
	}
}
