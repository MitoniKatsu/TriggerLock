/*
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the Edit Gun Window, and related methods and event handlers
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

public class EditGunWindow
{
	protected JInternalFrame frmEditGun;
	
	private JButton btnEditGunSave;
	private JButton btnEditGunCancel;
	
	private JComboBox<String> cmbxManufacturers;
	private JComboBox<String> cmbxCaliber;
	private JFormattedTextField fldModel;
	private JFormattedTextField fldLocation;
	private JLabel lblSerial;
	private String serial = "";

	public EditGunWindow()
	{
		//build window components
		frmEditGun = new JInternalFrame("Edit Gun");
		frmEditGun.setBounds(100, 100, 600,175);
		frmEditGun.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required fields");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(10,121,93,14);
		frmEditGun.getContentPane().add(lblRequired);
		
		JLabel lblManufacturer = new JLabel("*Manufacturer");
		lblManufacturer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblManufacturer.setBounds(247,36,93,14);
		frmEditGun.getContentPane().add(lblManufacturer);
		
		cmbxManufacturers = new JComboBox<String>();
		cmbxManufacturers.setBounds(350,33,226,20);
		frmEditGun.getContentPane().add(cmbxManufacturers);
		
		JLabel lblCaliber = new JLabel("*Caliber");
		lblCaliber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaliber.setBounds(-20,36,93,14);
		frmEditGun.getContentPane().add(lblCaliber);
		
		cmbxCaliber = new JComboBox<String>();
		cmbxCaliber.setBounds(83,33,167,20);
		frmEditGun.getContentPane().add(cmbxCaliber);
		
		lblSerial = new JLabel("*Serial:");
		lblSerial.setHorizontalAlignment(SwingConstants.LEFT);
		lblSerial.setBounds(38,11,212,14);
		frmEditGun.getContentPane().add(lblSerial);
		
		JLabel lblModel = new JLabel("*Model");
		lblModel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModel.setBounds(210,11,93,14);
		frmEditGun.getContentPane().add(lblModel);
		
		fldModel = new JFormattedTextField();
		fldModel.setBounds(313,8,263,20);
		frmEditGun.getContentPane().add(fldModel);
		fldModel.setDocument(new FieldLimit(30));
		
		JLabel lblLocation = new JLabel("*Location");
		lblLocation.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLocation.setBounds(-20,61,93,14);
		frmEditGun.getContentPane().add(lblLocation);
		
		fldLocation = new JFormattedTextField();
		fldLocation.setBounds(83,58,167,20);
		frmEditGun.getContentPane().add(fldLocation);
		fldLocation.setDocument(new FieldLimit(20));
		
		btnEditGunSave = new JButton("Save Changes");
		frmEditGun.getRootPane().setDefaultButton(btnEditGunSave);
		btnEditGunSave.setBounds(444,112,120,23);
		frmEditGun.getContentPane().add(btnEditGunSave);
		
		btnEditGunCancel = new JButton("Cancel");
		btnEditGunCancel.setBounds(344,112,90,23);
		frmEditGun.getContentPane().add(btnEditGunCancel);
				
		//event handlers
		frmEditGun.addComponentListener(new ComponentListener()
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
				clearEditGun();				
			}
		});
		
		btnEditGunCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				frmEditGun.setVisible(false);			
			}
		});
		
		btnEditGunSave.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (validateUpdateGun() != "")
				{
					QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
					newQuery.updateGun(validateUpdateGun());
					frmEditGun.setVisible(false);
				}				
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
	
	public void clearEditGun()
	{
		fldModel.setText("");
		lblSerial.setText("Serial: ");
		fldLocation.setText("");
		cmbxCaliber.removeAllItems();;
		cmbxManufacturers.removeAllItems();
	}
	
	//public methods
	public void parseLoadGun(ResultSet newload)
	{
		try
		{
			newload.next();
			//serial
			serial = newload.getString(1);
			lblSerial.setText("Serial: " + serial);
			//load combo boxes
			getComboBoxes();
			//find index of manufacturer and set index
			cmbxManufacturers.setSelectedIndex(findInComboBox(newload.getString(2), cmbxManufacturers));
			//model
			fldModel.setText(newload.getString(3));
			//find index of caliber and set index
			cmbxCaliber.setSelectedIndex(findInComboBox(newload.getString(4), cmbxCaliber));
			//location
			fldLocation.setText(newload.getString(5));			
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to parse result. Please try again, or see your system admin.");
		}
	}
	
	public int findInComboBox(String key, JComboBox<String> stringBox)
	{
		boolean match = false;
		
		for (int i = 0; i < stringBox.getItemCount(); i++)
		{
			if (key.length() == stringBox.getItemAt(i).toString().length())
			{
				//mark possible match and continue checking
				match = true;
				
				for (int j = 0; j < key.length(); j++)
				{
					if (match && key.charAt(j) == stringBox.getItemAt(i).toString().charAt(j))
					{
						match = true;
					}
					else
					{
						match = false;
					}
				}
				
				if (match)
				{
					return i;
				}
			}
		}
		
		return 0;
	}
	
	public String validateUpdateGun()
	{
		String updateQuery = "";
		String manufacturer = "";
		String caliber = "";
		String location = "";
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
			
			//pass serial
			serial = serial.toUpperCase();
			
			//validate location
			
			if (fldLocation.getText().trim().isEmpty())
			{
				valid = false;
				JOptionPane.showMessageDialog(null, "A valid location is required.");
			}
			else
			{
				//pass location
				location = fldLocation.getText().toUpperCase();
				
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
				}
			}
			
		}
		
		if (valid)
		{
			updateQuery = "UPDATE system.guns "
					+ "SET SERIAL = '" + serial
					+ "', MANUFACTURER = '" + manufacturer
					+ "', MODEL = '" + model
					+ "', CALIBER = '" + caliber
					+ "', LOCATION = '" + location
					+ "' WHERE SERIAL = '" + serial + "'";
		}
		else
		{
			return updateQuery;
		}
		
		return updateQuery;
	}

}
