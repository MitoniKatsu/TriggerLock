/*
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the Edit Ammo Window, and related methods and event handlers
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

public class EditAmmoWindow
{
	protected JInternalFrame frmEditAmmo;
	
	private JButton btnEditAmmoSave;
	private JButton btnEditAmmoCancel;
	
	private JComboBox<String> cmbxManufacturers;
	private JComboBox<String> cmbxCaliber;
	
	private JLabel lblModel;
	private JFormattedTextField fldQtyStocked;
	private JFormattedTextField fldCostPerRound;
	private String model = "";
	
	public EditAmmoWindow()
	{
		//build window components
		frmEditAmmo = new JInternalFrame("Edit Ammo");
		frmEditAmmo.setBounds(100, 100, 600, 200);
		frmEditAmmo.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required fields");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(10,141,93,14);
		frmEditAmmo.getContentPane().add(lblRequired);
		
		JLabel lblManufacturer = new JLabel("*Manufacturer");
		lblManufacturer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblManufacturer.setBounds(10,36,93,14);
		frmEditAmmo.getContentPane().add(lblManufacturer);
		
		cmbxManufacturers = new JComboBox<String>();
		cmbxManufacturers.setBounds(113,33,190,20);
		frmEditAmmo.getContentPane().add(cmbxManufacturers);
		
		JLabel lblCaliber = new JLabel("*Caliber");
		lblCaliber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaliber.setBounds(10,61,93,14);
		frmEditAmmo.getContentPane().add(lblCaliber);
		
		cmbxCaliber = new JComboBox<String>();
		cmbxCaliber.setBounds(113,58,190,20);
		frmEditAmmo.getContentPane().add(cmbxCaliber);
		
		lblModel = new JLabel("Model Number:");
		lblModel.setHorizontalAlignment(SwingConstants.LEFT);
		lblModel.setBounds(32,11,542,14);
		frmEditAmmo.getContentPane().add(lblModel);
		
		JLabel lblQtyStocked = new JLabel("Qty Stocked (defaults 0 if blank)");
		lblQtyStocked.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQtyStocked.setBounds(281,36,204,14);
		frmEditAmmo.getContentPane().add(lblQtyStocked);
		
		fldQtyStocked = new JFormattedTextField();
		fldQtyStocked.setBounds(495,33,79,20);
		frmEditAmmo.getContentPane().add(fldQtyStocked);
		fldQtyStocked.setDocument(new FieldLimit(6));
		
		JLabel lblCostPerRound = new JLabel("*Cost Per Round");
		lblCostPerRound.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCostPerRound.setBounds(392,61,93,14);
		frmEditAmmo.getContentPane().add(lblCostPerRound);
		
		fldCostPerRound = new JFormattedTextField();
		fldCostPerRound.setBounds(495,58,79,20);
		frmEditAmmo.getContentPane().add(fldCostPerRound);
		fldCostPerRound.setDocument(new FieldLimit(5));
		
		btnEditAmmoSave = new JButton("Save Changes");
		frmEditAmmo.getRootPane().setDefaultButton(btnEditAmmoSave);
		btnEditAmmoSave.setBounds(454,137,120,23);
		frmEditAmmo.getContentPane().add(btnEditAmmoSave);
		
		btnEditAmmoCancel = new JButton("Cancel");
		btnEditAmmoCancel.setBounds(354,137,90,23);
		frmEditAmmo.getContentPane().add(btnEditAmmoCancel);

		
		//event handlers
		frmEditAmmo.addComponentListener(new ComponentListener()
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
				clearEditAmmo();
				
			}
		});
		
		btnEditAmmoCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				frmEditAmmo.setVisible(false);			
			}
		});
		
		btnEditAmmoSave.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (validateUpdateAmmo() != "")
				{
					QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
					newQuery.updateAmmo(validateUpdateAmmo());
					frmEditAmmo.setVisible(false);
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
	
	public void clearEditAmmo()
	{
		fldCostPerRound.setText("");
		lblModel.setText("Model: ");
		fldQtyStocked.setText("");
		cmbxCaliber.removeAllItems();;
		cmbxManufacturers.removeAllItems();
	}
	
	//public methods
	
	public void parseLoadAmmo(ResultSet newload)
	{
		try
		{
			newload.next();
			//model number
			model = newload.getString(3);
			lblModel.setText("Model: " + model);
			//load combo boxes
			getComboBoxes();
			//find index of manufacturer and set index
			cmbxManufacturers.setSelectedIndex(findInComboBox(newload.getString(1), cmbxManufacturers));
			//find index of caliber and set index
			cmbxCaliber.setSelectedIndex(findInComboBox(newload.getString(2), cmbxCaliber));
			//qty stocked
			fldQtyStocked.setText(newload.getString(4));
			//cost per round
			fldCostPerRound.setText(newload.getString(5));
			
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
	
	public String validateUpdateAmmo()
	{
		String updateQuery = "";
		String manufacturer = "";
		String caliber = "";
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
			
			
			//pass model
			model = model.toUpperCase();
			
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
		
		if (valid)
		{
			updateQuery = "UPDATE system.ammo "
					+ "SET MANUFACTURER = '" + manufacturer 
					+ "', CALIBER = '" + caliber
					+ "', MODEL = '" + model
					+ "', QTY_STOCKED = " + qtyStocked 
					+ ", COST_PER_ROUND = " + costPerRd
					+ " WHERE MODEL = '" + model + "'";
		}
		else
		{
			return updateQuery;
		}
		
		return updateQuery;
	}
}
