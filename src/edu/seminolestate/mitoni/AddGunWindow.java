/*
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the Add Gun Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class AddGunWindow
{
	protected JInternalFrame frmAddGun;
	
	private JButton btnAddGunSubmit;
	private JButton btnAddGunCancel;
	
	private JComboBox<String> cmbxManufacturers;
	private JComboBox<String> cmbxCaliber;
	
	private JFormattedTextField fldSerial;
	private JFormattedTextField fldModel;

	public AddGunWindow()
	{
		//build window components
		frmAddGun = new JInternalFrame("Add Gun");
		frmAddGun.setBounds(100, 100, 600,150);
		frmAddGun.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required fields");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(10,87,93,14);
		frmAddGun.getContentPane().add(lblRequired);
		
		JLabel lblManufacturer = new JLabel("*Manufacturer");
		lblManufacturer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblManufacturer.setBounds(247,36,93,14);
		frmAddGun.getContentPane().add(lblManufacturer);
		
		cmbxManufacturers = new JComboBox<String>();
		cmbxManufacturers.setBounds(350,33,226,20);
		frmAddGun.getContentPane().add(cmbxManufacturers);
		
		JLabel lblCaliber = new JLabel("*Caliber");
		lblCaliber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaliber.setBounds(-20,36,93,14);
		frmAddGun.getContentPane().add(lblCaliber);
		
		cmbxCaliber = new JComboBox<String>();
		cmbxCaliber.setBounds(83,33,167,20);
		frmAddGun.getContentPane().add(cmbxCaliber);
		
		JLabel lblSerial = new JLabel("*Serial");
		lblSerial.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSerial.setBounds(-20,11,93,14);
		frmAddGun.getContentPane().add(lblSerial);
		
		fldSerial = new JFormattedTextField();
		fldSerial.setBounds(83,8,167,20);
		frmAddGun.getContentPane().add(fldSerial);
		fldSerial.setDocument(new FieldLimit(20));
		
		JLabel lblModel = new JLabel("*Model");
		lblModel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModel.setBounds(210,11,93,14);
		frmAddGun.getContentPane().add(lblModel);
		
		fldModel = new JFormattedTextField();
		fldModel.setBounds(313,8,263,20);
		frmAddGun.getContentPane().add(fldModel);
		fldModel.setDocument(new FieldLimit(30));
		
		btnAddGunSubmit = new JButton("Submit");
		frmAddGun.getRootPane().setDefaultButton(btnAddGunSubmit);
		btnAddGunSubmit.setBounds(486,83,90,23);
		frmAddGun.getContentPane().add(btnAddGunSubmit);
		
		btnAddGunCancel = new JButton("Cancel");
		btnAddGunCancel.setBounds(380,83,90,23);
		frmAddGun.getContentPane().add(btnAddGunCancel);
		
		//event handlers
		frmAddGun.addComponentListener(new ComponentListener()
		{
			
			@Override
			public void componentShown(ComponentEvent e)
			{
				//populate combo boxes
				getComboBoxes();
				
			}
			
			@Override
			public void componentResized(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e)
			{
				clearAddGun();
				
			}
		});
		
		btnAddGunSubmit.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (validateGun() != "")
				{
					QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
					newQuery.addGun(validateGun());
					frmAddGun.setVisible(false);
				}
			}
		});
		
		btnAddGunCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				frmAddGun.setVisible(false);
			}
		});
	}
	
	//getters and setters
	
	private void getComboBoxes()
	{
		getManufacturers();
		getCalibers();
	}
	
	private  void getManufacturers()
	{
		QueryManufacturer manfQuery = new QueryManufacturer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet rs = manfQuery.listManufacturers();
		cmbxManufacturers.addItem("Select One");
		int index = 1;
		try
		{
			while (rs.next())
			{
				cmbxManufacturers.addItem(rs.getString(index));;
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			manfQuery.closeSQL();
		}
		
		
	}
	
	private void getCalibers()
	{
		QueryCaliber CalQuery = new QueryCaliber(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet rs = CalQuery.listCalibers();
		cmbxCaliber.addItem("Select One");
		int index = 1;
		try
		{
			while (rs.next())
			{
				cmbxCaliber.addItem(rs.getString(index));;
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			CalQuery.closeSQL();
		}
	}
	
	private void clearAddGun()
	{
		fldSerial.setText("");
		fldModel.setText("");
		cmbxCaliber.removeAllItems();
		cmbxManufacturers.removeAllItems();
	}
	
	//public methods
	
	public String validateGun()
	{
		String insertQuery = "";
		String manufacturer = "";
		String caliber = "";
		String serial = "";
		String model = "";
		
		boolean valid = true;
		
		//validate comboboxes
		if(cmbxManufacturers.getSelectedIndex() == 0 && cmbxCaliber.getSelectedIndex() == 0)
		{
			valid = false;
			JOptionPane.showMessageDialog(null, "A valid manufacturer and caliber are required.");
		}
		else
		{
			//pass combo boxes
			manufacturer = cmbxManufacturers.getSelectedItem().toString().toUpperCase();
			caliber = cmbxCaliber.getSelectedItem().toString().toUpperCase();
			
			//validate model
			if (fldModel.getText().trim().isEmpty())
			{
				valid = false;
				JOptionPane.showMessageDialog(null, "A valid model is required.");
			}
			else
			{
				//pass model
				model = fldModel.getText().toUpperCase();
				
				//validate serial
				if (fldSerial.getText().trim().isEmpty())
				{
					valid = false;
					JOptionPane.showMessageDialog(null, "A valid serial is required.");
				}
				else
				{
					//pass serial
					serial = fldSerial.getText().toUpperCase();
				}
			}
		}
		
		if (valid)
		{
			insertQuery = "INSERT INTO SYSTEM.GUNS (serial, manufacturer, model, caliber, location)"
					+ " VALUES ('" + serial + "', '" + manufacturer + "', '" + model + "', '" + caliber + "', 'LOCKUP')";
		}
		else
		{
			return insertQuery;
		}
		
		return insertQuery;
	}
}
