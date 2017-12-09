/*
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the Add Ammo Window, and related methods and event handlers
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

public class AddAmmoWindow
{
	protected JInternalFrame frmAddAmmo;
	
	private JButton btnAddAmmoSubmit;
	private JButton btnAddAmmoCancel;
	
	private JComboBox<String> cmbxManufacturers;
	private JComboBox<String> cmbxCaliber;
	
	private JFormattedTextField fldModel;
	private JFormattedTextField fldQtyStocked;
	private JFormattedTextField fldCostPerRound;	
	
	public AddAmmoWindow()
	{
		//build window components
		frmAddAmmo = new JInternalFrame("Add Ammo");
		frmAddAmmo.setBounds(100, 100, 600, 200);
		frmAddAmmo.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required fields");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(10,141,93,14);
		frmAddAmmo.getContentPane().add(lblRequired);
		
		JLabel lblManufacturer = new JLabel("*Manufacturer");
		lblManufacturer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblManufacturer.setBounds(10,36,93,14);
		frmAddAmmo.getContentPane().add(lblManufacturer);
		
		cmbxManufacturers = new JComboBox<String>();
		cmbxManufacturers.setBounds(113,33,190,20);
		frmAddAmmo.getContentPane().add(cmbxManufacturers);
		
		JLabel lblCaliber = new JLabel("*Caliber");
		lblCaliber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaliber.setBounds(10,61,93,14);
		frmAddAmmo.getContentPane().add(lblCaliber);
		
		cmbxCaliber = new JComboBox<String>();
		cmbxCaliber.setBounds(113,58,190,20);
		frmAddAmmo.getContentPane().add(cmbxCaliber);
		
		JLabel lblModel = new JLabel("*Model Number");
		lblModel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModel.setBounds(10,11,93,14);
		frmAddAmmo.getContentPane().add(lblModel);
		
		fldModel = new JFormattedTextField();
		fldModel.setBounds(113,8,461,20);
		frmAddAmmo.getContentPane().add(fldModel);
		fldModel.setDocument(new FieldLimit(30));
		
		JLabel lblQtyStocked = new JLabel("Qty Stocked (defaults 0 if blank)");
		lblQtyStocked.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQtyStocked.setBounds(281,36,204,14);
		frmAddAmmo.getContentPane().add(lblQtyStocked);
		
		fldQtyStocked = new JFormattedTextField();
		fldQtyStocked.setBounds(495,33,79,20);
		frmAddAmmo.getContentPane().add(fldQtyStocked);
		fldQtyStocked.setDocument(new FieldLimit(6));
		
		JLabel lblCostPerRound = new JLabel("*Cost Per Round");
		lblCostPerRound.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCostPerRound.setBounds(392,61,93,14);
		frmAddAmmo.getContentPane().add(lblCostPerRound);
		
		fldCostPerRound = new JFormattedTextField();
		fldCostPerRound.setBounds(495,58,79,20);
		frmAddAmmo.getContentPane().add(fldCostPerRound);
		fldCostPerRound.setDocument(new FieldLimit(5));
		
		btnAddAmmoSubmit = new JButton("Submit");
		frmAddAmmo.getRootPane().setDefaultButton(btnAddAmmoSubmit);
		btnAddAmmoSubmit.setBounds(484,137,90,23);
		frmAddAmmo.getContentPane().add(btnAddAmmoSubmit);
		
		btnAddAmmoCancel = new JButton("Cancel");
		btnAddAmmoCancel.setBounds(384,137,90,23);
		frmAddAmmo.getContentPane().add(btnAddAmmoCancel);

		
		//event handlers
		frmAddAmmo.addComponentListener(new ComponentListener()
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
				clearAddAmmo();
				
			}
		});
		
		btnAddAmmoCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				frmAddAmmo.setVisible(false);			
			}
		});
		
		btnAddAmmoSubmit.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (validateAddAmmo() != "")
				{
					QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
					newQuery.addAmmo(validateAddAmmo());
					frmAddAmmo.setVisible(false);
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
	
	public void clearAddAmmo()
	{
		fldCostPerRound.setText("");
		fldModel.setText("");
		fldQtyStocked.setText("");
		cmbxCaliber.removeAllItems();;
		cmbxManufacturers.removeAllItems();
	}
	
	public String validateAddAmmo()
	{
		String insertQuery = "";
		String manufacturer = "";
		String caliber = "";
		String model = "";
		int qtyStocked = 0;
		float costPerRd = 0;
		
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
				
				//validate qty stocked
				if (!fldQtyStocked.getText().trim().isEmpty())
				{
					try
					{
						qtyStocked = Integer.parseInt(fldQtyStocked.getText().trim());
					}
					catch (NumberFormatException e)
					{
						valid = false;
						JOptionPane.showMessageDialog(null, "Qty stocked must be a valid whole number entry, or blank.");
					}
				}
				
				//validate cost per round
				if (valid && !fldCostPerRound.getText().trim().isEmpty())
				{
					try
					{
						costPerRd = Float.parseFloat(fldCostPerRound.getText().trim());
					}
					catch (NumberFormatException e)
					{
						valid = false;
						JOptionPane.showMessageDialog(null, "Cost per round must be a valid decimal number.");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "A valid cost per round is required.");
					valid = false;
				}
				
			}
		}
		
		if (valid)
		{
			insertQuery = "INSERT INTO SYSTEM.AMMO (manufacturer, caliber, model, qty_stocked, cost_per_round)"
					+ " VALUES ('" + manufacturer + "', '" + caliber + "', '" + model + "', " + qtyStocked + ", " + costPerRd + ")";
		}
		else
		{
			return insertQuery;
		}
		
		return insertQuery;
	}

}
