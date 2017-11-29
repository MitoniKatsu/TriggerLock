/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the Add Caliber Window, and menu related event handlers
 */

package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class AddCaliberWindow
{
	protected JInternalFrame frmAddCaliber;
	
	private JButton btnAddCaliberSubmit;
	private JButton btnAddCaliberCancel;
	
	private JFormattedTextField fldCaliber;
	

	public AddCaliberWindow()
	{
		//build window components
		frmAddCaliber = new JInternalFrame("Add New Caliber");
		frmAddCaliber.setBounds(100, 100, 200, 150);
		frmAddCaliber.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("* required field");
		lblRequired.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRequired.setBounds(65,36,93,14);
		frmAddCaliber.getContentPane().add(lblRequired);
		
		JLabel lblCaliber = new JLabel("*New Caliber");
		lblCaliber.setVerticalAlignment(SwingConstants.BOTTOM);
		lblCaliber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaliber.setBounds(-19,11,93,14);
		frmAddCaliber.getContentPane().add(lblCaliber);
		
		fldCaliber = new JFormattedTextField();
		fldCaliber.setBounds(81,8,93,20);
		frmAddCaliber.getContentPane().add(fldCaliber);
		fldCaliber.setDocument(new FieldLimit(16));
		
		btnAddCaliberSubmit = new JButton("Submit");
		frmAddCaliber.getRootPane().setDefaultButton(btnAddCaliberSubmit);
		btnAddCaliberSubmit.setBounds(99,87,75,23);
		frmAddCaliber.getContentPane().add(btnAddCaliberSubmit);
		
		btnAddCaliberCancel = new JButton("Cancel");
		btnAddCaliberCancel.setBounds(10,87,75,23);
		frmAddCaliber.getContentPane().add(btnAddCaliberCancel);
		
		//Event handlers
		
		btnAddCaliberSubmit.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if (!fldCaliber.getText().trim().isEmpty())
				{
					QueryCaliber newQuery = new QueryCaliber(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
					String insertCaliber = "INSERT INTO SYSTEM.CALIBER (caliber)"
							+ "VALUES ('" + fldCaliber.getText().trim().toUpperCase() + "')";
					
					newQuery.addCaliber(insertCaliber);
					fldCaliber.setText("");
				}
				else
				{
					JOptionPane.showMessageDialog(null,	"Caliber field is required. Please make a valid entry.");
				}
			}
		});
		
		btnAddCaliberCancel.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				fldCaliber.setText("");
				frmAddCaliber.setVisible(false);
			}
		});
	}

}
