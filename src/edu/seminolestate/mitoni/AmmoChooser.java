/*
 * Written by Christian Lundblad
 * December 8, 2017
 * This class contains the Ammo Chooser Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

public class AmmoChooser
{
	protected JInternalFrame frmAmmoChooser;
	
	private JComboBox<String> cmbxCalibers;
	private JComboBox<String> cmbxManufacturers;
	private JComboBox<String> cmbxModel;
	private JComboBox<String> cmbxQtyBoxes10;
	
	private JButton btnConfirmAmmoChoice;
	private JButton btnCancelAmmoChoice;
	
	private String caliberChosen = "";
	private String manufacturerChosen = "";
	private String modelChosen = "";
	private double costChosen = 0;
	private int qtyChosen = 0;
	private int chosenStocked = 0;
	
	private NumberFormat c = NumberFormat.getCurrencyInstance();
	
	public AmmoChooser()
	{
		//build window components
		frmAmmoChooser = new JInternalFrame("Choose Ammunition");
		frmAmmoChooser.setBounds(50, 50, 500, 200);
		frmAmmoChooser.getContentPane().setLayout(null);
		
		cmbxCalibers = new JComboBox<String>();
		cmbxCalibers.setBounds(10,11,464,20);
		frmAmmoChooser.getContentPane().add(cmbxCalibers);
		
		cmbxManufacturers = new JComboBox<String>();
		cmbxManufacturers.setBounds(10,42,464,20);
		frmAmmoChooser.getContentPane().add(cmbxManufacturers);
		
		cmbxModel = new JComboBox<String>();
		cmbxModel.setBounds(10,73,464,20);
		frmAmmoChooser.getContentPane().add(cmbxModel);
		
		cmbxQtyBoxes10 = new JComboBox<String>();
		cmbxQtyBoxes10.setBounds(10,104,464,20);
		frmAmmoChooser.getContentPane().add(cmbxQtyBoxes10);

		btnConfirmAmmoChoice = new JButton("Confirm");
		btnConfirmAmmoChoice.setEnabled(false);
		btnConfirmAmmoChoice.setBounds(384,135,90,23);
		frmAmmoChooser.getContentPane().add(btnConfirmAmmoChoice);
		btnConfirmAmmoChoice.getRootPane().setDefaultButton(btnConfirmAmmoChoice);
		
		btnCancelAmmoChoice = new JButton("Cancel");
		btnCancelAmmoChoice.setBounds(284,135,90,23);
		frmAmmoChooser.getContentPane().add(btnCancelAmmoChoice);
		
		//event handlers
		frmAmmoChooser.addComponentListener(new ComponentListener()
		{
			
			@Override
			public void componentShown(ComponentEvent e)
			{
				loadCalibers();
			}
			
			@Override
			public void componentResized(ComponentEvent e)
			{
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e)
			{
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e)
			{
				//clear all comboboxes
				cmbxManufacturers.removeAllItems();
				cmbxCalibers.removeAllItems();
				cmbxModel.removeAllItems();
				cmbxQtyBoxes10.removeAllItems();
				//clear variable Strings
				manufacturerChosen = "";
				caliberChosen = "";
				modelChosen = "";
				qtyChosen = 0;
				chosenStocked = 0;
			}
		});
		
		cmbxCalibers.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				btnConfirmAmmoChoice.setEnabled(false);
				cmbxManufacturers.removeAllItems();
				cmbxModel.removeAllItems();
				cmbxQtyBoxes10.removeAllItems();
				if (cmbxCalibers.getItemCount() > 1 && cmbxCalibers.getSelectedIndex() != 0)
				{
					caliberChosen = cmbxCalibers.getSelectedItem().toString();
					loadManufacturers();
				}
				
			}
		});
		
		cmbxManufacturers.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				btnConfirmAmmoChoice.setEnabled(false);
				cmbxModel.removeAllItems();
				cmbxQtyBoxes10.removeAllItems();
				if (cmbxManufacturers.getItemCount() > 1 && cmbxManufacturers.getSelectedIndex() != 0)
				{
					manufacturerChosen = cmbxManufacturers.getSelectedItem().toString();
					loadModels();
				}
				
			}
		});
		
		cmbxModel.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				btnConfirmAmmoChoice.setEnabled(false);
				cmbxQtyBoxes10.removeAllItems();
				if (cmbxModel.getItemCount() > 1 && cmbxModel.getSelectedIndex() != 0)
				{
					modelChosen = cmbxModel.getSelectedItem().toString();
					loadQty();
				}
				
			}
		});
		
		cmbxQtyBoxes10.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (cmbxQtyBoxes10.getItemCount() > 1 && cmbxQtyBoxes10.getSelectedIndex() != 0)
				{
					qtyChosen = cmbxQtyBoxes10.getSelectedIndex() * 10;
					btnConfirmAmmoChoice.setEnabled(true);
				}
				
			}
		});
		
		btnConfirmAmmoChoice.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.addrangeVisitWindow.setChosenAmmo(modelChosen, costChosen, qtyChosen, chosenStocked);
				MainApplication.addrangeVisitWindow.updateLabels();
				frmAmmoChooser.setVisible(false);			
			}
		});
		
		btnCancelAmmoChoice.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frmAmmoChooser.setVisible(false);
				MainApplication.addrangeVisitWindow.cancelAmmoSale();
			}
		});
	}

	//getters and setters
	
	//methods
	private void loadCalibers()
	{
		cmbxCalibers.addItem("Select Caliber");
		
		QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet calResult = newQuery.narrowByCaliber();
		
		try
		{
			while (calResult.next())
			{
				cmbxCalibers.addItem(calResult.getString(1));
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error loading results.");
		}
		finally
		{
			newQuery.closeSQL();
		}
	}
	
	private void loadManufacturers()
	{
		QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet manfResult = newQuery.narrowByManufacturer(caliberChosen);
		
		cmbxManufacturers.addItem("Select Manufacturer");
		
		try
		{				
			while (manfResult.next())
			{
				cmbxManufacturers.addItem(manfResult.getString(1));
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error loading results.");
		}
		finally
		{
			newQuery.closeSQL();
		}
	}
	
	private void loadModels()
	{
		QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet modelResult = newQuery.narrowByModel(caliberChosen, manufacturerChosen);
		
		cmbxModel.addItem("Select Model");
		
		try
		{				
			while (modelResult.next())
			{
				cmbxModel.addItem(modelResult.getString(1));
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error loading results.");
		}
		finally
		{
			newQuery.closeSQL();
		}
	}
	
	private void loadQty()
	{
		QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet qtyResult = newQuery.narrowByQtyStocked(caliberChosen, manufacturerChosen, modelChosen);
		
		cmbxQtyBoxes10.addItem("Select Quantity");
		
		try
		{				
			qtyResult.next();
			
			chosenStocked = qtyResult.getInt(1);
			modelChosen = qtyResult.getString(2);
			costChosen = qtyResult.getDouble(3);
			
			for (int i = 10; i <= qtyResult.getInt(1); i+=10)
			{
				cmbxQtyBoxes10.addItem(i + " rounds - price: " + c.format((i * qtyResult.getDouble(3)) * 1.2));
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error loading results.");
		}
		finally
		{
			newQuery.closeSQL();
		}
	}

	
	
}
