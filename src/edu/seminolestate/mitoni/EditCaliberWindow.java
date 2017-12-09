/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the Edit Caliber Window, and related methods and event handlers
 */

package edu.seminolestate.mitoni;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class EditCaliberWindow
{
	JInternalFrame frmEditCaliber;
	
	private JButton btnEditCaliberSave;
	private JButton btnEditCaliberCancel;
	
	private JFormattedTextField fldCaliber;
	private String caliber;

	public EditCaliberWindow()
	{
		//build window components
		frmEditCaliber = new JInternalFrame("Edit Caliber");
		frmEditCaliber.setBounds(100, 100, 250, 150);
		frmEditCaliber.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required field");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(131,39,93,14);
		frmEditCaliber.getContentPane().add(lblRequired);
		
		JLabel lblCaliber = new JLabel("*New Caliber");
		lblCaliber.setVerticalAlignment(SwingConstants.BOTTOM);
		lblCaliber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaliber.setBounds(-19,11,93,14);
		frmEditCaliber.getContentPane().add(lblCaliber);
		
		fldCaliber = new JFormattedTextField();
		fldCaliber.setBounds(81,8,143,20);
		frmEditCaliber.getContentPane().add(fldCaliber);
		fldCaliber.setDocument(new FieldLimit(16));
		
		btnEditCaliberSave = new JButton("Save Changes");
		frmEditCaliber.getRootPane().setDefaultButton(btnEditCaliberSave);
		btnEditCaliberSave.setBounds(98,87,126,23);
		frmEditCaliber.getContentPane().add(btnEditCaliberSave);
		
		btnEditCaliberCancel = new JButton("Cancel");
		btnEditCaliberCancel.setBounds(10,87,78,23);
		frmEditCaliber.getContentPane().add(btnEditCaliberCancel);
		
		//Event handlers
		
		btnEditCaliberSave.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (!fldCaliber.getText().trim().isEmpty())
				{
					QueryCaliber newQuery = new QueryCaliber(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
					String updateCaliber = "UPDATE SYSTEM.CALIBER"
					+ " SET CALIBER = '" + fldCaliber.getText().trim().toUpperCase() + "'"
					+ " WHERE CALIBER = '" + caliber + "'";
					
					newQuery.updateCaliber(updateCaliber);
					clear();
				}
				else
				{
					JOptionPane.showMessageDialog(null,	"Caliber field is required. Please make a valid entry.");
				}
			}
		});
		
		btnEditCaliberCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				fldCaliber.setText("");
				frmEditCaliber.setVisible(false);
			}
		});
	
	}
	
	//getters and setters
	
	public String getFldCaliber()
	{
		return fldCaliber.getText();
	}
	
	//public methods
	public void clear()
	{
		fldCaliber.setText("");
		frmEditCaliber.setVisible(false);
	}
	
	public void parseLoadCaliber(ResultSet newload)
	{
		try
		{
			newload.next();
			//caliber
			caliber = newload.getString(1);
			fldCaliber.setText(caliber);
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to parse result. Please try again, or see your system admin.");
		}
	}
	

}
