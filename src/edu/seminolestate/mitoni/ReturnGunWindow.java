/*
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the Return Gun Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class ReturnGunWindow
{
	protected JInternalFrame frmReturnGun;
	
	private JButton btnReturnGunReturn;
	private JButton btnReturnGunCancel;
	
	JFormattedTextField fldSerial;
	JRadioButton radFromRange;
	JRadioButton radFromCleaning;

	public ReturnGunWindow()
	{
		//build window components
		frmReturnGun = new JInternalFrame("Return Gun");
		frmReturnGun.setBounds(100, 100, 300, 200);
		frmReturnGun.getContentPane().setLayout(null);
		
		radFromRange = new JRadioButton("Return from range");
		radFromRange.setSelected(true);
		radFromRange.setBounds(80, 45, 170, 23);
		frmReturnGun.getContentPane().add(radFromRange);
		
		radFromCleaning = new JRadioButton("Return from cleaning");
		radFromCleaning.setBounds(80, 71, 170, 23);
		frmReturnGun.getContentPane().add(radFromCleaning);

		ButtonGroup grpReturnGun = new ButtonGroup();
		grpReturnGun.add(radFromRange);
		grpReturnGun.add(radFromCleaning);
		
		JLabel lblSerial = new JLabel("*Serial");
		lblSerial.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSerial.setBounds(-20,11,93,14);
		frmReturnGun.getContentPane().add(lblSerial);
		
		fldSerial = new JFormattedTextField();
		fldSerial.setBounds(83,8,167,20);
		frmReturnGun.getContentPane().add(fldSerial);
		fldSerial.setDocument(new FieldLimit(20));
		
		JLabel lblRequired = new JLabel("* required fields");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(10,112,93,14);
		frmReturnGun.getContentPane().add(lblRequired);
		
		btnReturnGunReturn = new JButton("Return");
		frmReturnGun.getRootPane().setDefaultButton(btnReturnGunReturn);
		btnReturnGunReturn.setBounds(184,137,90,23);
		frmReturnGun.getContentPane().add(btnReturnGunReturn);
		
		btnReturnGunCancel = new JButton("Cancel");
		btnReturnGunCancel.setBounds(84,137,90,23);
		frmReturnGun.getContentPane().add(btnReturnGunCancel);
		
		//event handlers
		
		btnReturnGunReturn.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (validateSerial())
				{
					QueryGun returnQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
					
					if (radFromRange.isSelected())
					{
						returnQuery.returnGun(fldSerial.getText().trim(), "cleaning");
					}
					else
					{
						returnQuery.returnGun(fldSerial.getText().trim(), "lockup");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Serial "+ fldSerial.getText().trim().toUpperCase() +" is not a valid gun on record. Please recheck the serial, or see management.");
				}	
				
				clearReturnWindow();
				frmReturnGun.setVisible(false);
			}
		});
		
		btnReturnGunCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				clearReturnWindow();
				frmReturnGun.setVisible(false);
			}
		});

	}
	
	//getters and setters
	
	public void clearReturnWindow()
	{
		fldSerial.setText("");
		radFromRange.setSelected(true);
	}
	
	//public methods
	public boolean validateSerial()
	{
		boolean validSerial = true;
		
		//query guns list for serials
		QueryGun serialQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet gunsList = serialQuery.listGuns();
		
		try
		{
			while (gunsList.next())
			{
				if (fldSerial.getText().trim().length() == gunsList.getString(1).length())
				{
					//mark possible match and continue checking
					validSerial = true;
					
					for (int j = 0; j < fldSerial.getText().trim().length(); j++)
					{
						if (validSerial && fldSerial.getText().trim().toUpperCase().charAt(j) == gunsList.getString(1).charAt(j))
						{
							validSerial = true;
						}
						else
						{
							validSerial = false;
						}
					}	
					
					if (validSerial)
					{
						return true;
					}
				}
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to parse guns list.");
		}
		finally
		{
			serialQuery.closeSQL();	
		}
		
		return false;
	}
	

}
