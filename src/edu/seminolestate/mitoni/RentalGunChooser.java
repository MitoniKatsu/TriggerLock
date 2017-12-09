/*
 * Written by Christian Lundblad
 * December 8, 2017
 * This class contains the Rental Gun Chooser Window, and related methods and event handlers
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

public class RentalGunChooser
{
	protected JInternalFrame frmRentalGunChooser;
	
	private JComboBox<String> cmbxManufacturers;
	private JComboBox<String> cmbxCalibers;
	private JComboBox<String> cmbxModels;
	private JComboBox<String> cmbxSerials;
	
	private JButton btnConfirmGunChoice;
	private JButton btnCancelGunChoice;
	
	private String manufacturerChosen = "";
	private String caliberChosen = "";
	private String modelChosen = "";
	private String serialChosen = "";
	
	public RentalGunChooser()
	{
		//build window components
		frmRentalGunChooser = new JInternalFrame("Choose Rental Gun");
		frmRentalGunChooser.setBounds(50, 50, 250, 205);
		frmRentalGunChooser.getContentPane().setLayout(null);
		
		cmbxManufacturers = new JComboBox<String>();
		cmbxManufacturers.setBounds(10,11,214,20);
		frmRentalGunChooser.getContentPane().add(cmbxManufacturers);

		cmbxCalibers = new JComboBox<String>();
		cmbxCalibers.setBounds(10,42,214,20);
		frmRentalGunChooser.getContentPane().add(cmbxCalibers);
		
		cmbxModels = new JComboBox<String>();
		cmbxModels.setBounds(10,73,214,20);
		frmRentalGunChooser.getContentPane().add(cmbxModels);
		
		cmbxSerials = new JComboBox<String>();
		cmbxSerials.setBounds(10,104,214,20);
		frmRentalGunChooser.getContentPane().add(cmbxSerials);
		
		btnConfirmGunChoice = new JButton("Confirm");
		btnConfirmGunChoice.setEnabled(false);
		btnConfirmGunChoice.setBounds(134,135,90,23);
		frmRentalGunChooser.getContentPane().add(btnConfirmGunChoice);
		frmRentalGunChooser.getRootPane().setDefaultButton(btnConfirmGunChoice);
		
		btnCancelGunChoice = new JButton("Cancel");
		btnCancelGunChoice.setBounds(34,135,90,23);
		frmRentalGunChooser.getContentPane().add(btnCancelGunChoice);
		
		//event handlers
		frmRentalGunChooser.addComponentListener(new ComponentListener()
		{
			
			@Override
			public void componentShown(ComponentEvent e)
			{
				loadManufacturers();
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
				cmbxModels.removeAllItems();
				cmbxSerials.removeAllItems();
				//clear variable Strings
				manufacturerChosen = "";
				caliberChosen = "";
				modelChosen = "";
				serialChosen = "";
			}
		});	
		
		cmbxManufacturers.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				btnConfirmGunChoice.setEnabled(false);
				cmbxCalibers.removeAllItems();
				cmbxModels.removeAllItems();
				cmbxSerials.removeAllItems();
				if (cmbxManufacturers.getItemCount() > 1 && cmbxManufacturers.getSelectedIndex() != 0)
				{
					manufacturerChosen = cmbxManufacturers.getSelectedItem().toString();
					loadCalibers();
				}
				
			}
		});
		
		cmbxCalibers.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				btnConfirmGunChoice.setEnabled(false);
				cmbxModels.removeAllItems();
				cmbxSerials.removeAllItems();
				if (cmbxCalibers.getItemCount() > 1 && cmbxCalibers.getSelectedIndex() != 0)
				{
					caliberChosen = cmbxCalibers.getSelectedItem().toString();
					loadModels();
				}
				
			}
		});
		
		cmbxModels.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				btnConfirmGunChoice.setEnabled(false);
				cmbxSerials.removeAllItems();
				if (cmbxModels.getItemCount() > 1 && cmbxModels.getSelectedIndex() != 0)
				{
					modelChosen = cmbxModels.getSelectedItem().toString();
					loadSerials();
				}
				
			}
		});
		
		cmbxSerials.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (cmbxSerials.getItemCount() > 1 && cmbxSerials.getSelectedIndex() != 0)
				{
					serialChosen = cmbxSerials.getSelectedItem().toString();
					btnConfirmGunChoice.setEnabled(true);
				}
				
			}
		});
		
		btnConfirmGunChoice.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.addrangeVisitWindow.setRentalSerial(serialChosen);
				frmRentalGunChooser.setVisible(false);			
			}
		});
		
		btnCancelGunChoice.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frmRentalGunChooser.setVisible(false);
				MainApplication.addrangeVisitWindow.cancelGunRental();
			}
		});
	}
	
	//getters and setters
	
	//methods
	private void loadManufacturers()
	{
		cmbxManufacturers.addItem("Select Manufacturer");
		
		QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet manfResult = newQuery.narrowByManufacturer();
		
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
	
	private void loadCalibers()
	{
		QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet calResult = newQuery.narrowByCaliber(manufacturerChosen);
		
		cmbxCalibers.addItem("Select Caliber");
		
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
	
	private void loadModels()
	{
		QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet modelResult = newQuery.narrowByModel(manufacturerChosen, caliberChosen);
		
		cmbxModels.addItem("Select Model");
		
		try
		{				
			while (modelResult.next())
			{
				cmbxModels.addItem(modelResult.getString(1));
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
	
	private void loadSerials()
	{
		QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet serialResult = newQuery.narrowBySerial(manufacturerChosen, caliberChosen, modelChosen);
		
		cmbxSerials.addItem("Select Serial Number");
		
		try
		{				
			while (serialResult.next())
			{
				cmbxSerials.addItem(serialResult.getString(1));
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
