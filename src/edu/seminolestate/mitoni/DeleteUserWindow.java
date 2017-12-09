/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the delete user window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class DeleteUserWindow
{
	protected JInternalFrame frmDeleteUser;
	private JButton btnDeleteUser;
	private JButton btnCancelDeleteUser;
	
	private JFormattedTextField fldDeleteUsername;
	
	public DeleteUserWindow()
	{
		//build window components
		frmDeleteUser = new JInternalFrame("Delete User");
		frmDeleteUser.setBounds(200, 200, 246, 123);
		frmDeleteUser.getContentPane().setLayout(null);

		JLabel username = new JLabel("Username");
		username.setHorizontalAlignment(SwingConstants.RIGHT);
		username.setBounds(10, 14, 60, 14);
		frmDeleteUser.getContentPane().add(username);

		fldDeleteUsername = new JFormattedTextField();
		fldDeleteUsername.setBounds(80, 11, 100, 20);
		frmDeleteUser.getContentPane().add(fldDeleteUsername);

		btnDeleteUser = new JButton("Delete");
		btnDeleteUser.setBounds(20, 60, 89, 23);
		frmDeleteUser.getContentPane().add(btnDeleteUser);

		btnCancelDeleteUser = new JButton("Cancel");
		btnCancelDeleteUser.setBounds(119, 60, 89, 23);
		frmDeleteUser.getContentPane().add(btnCancelDeleteUser);
		
		//Event Handlers
		
		btnDeleteUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				UserQuery deleteUser = new UserQuery(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				deleteUser.dropUser(getfldDeleteUserValue());
				clearFldDeleteUsername();
				frmDeleteUser.setVisible(false);
			}
		});

		btnCancelDeleteUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clearFldDeleteUsername();
				frmDeleteUser.setVisible(false);
			}
		});
	}
	
	//Getters-Setters
	public String getfldDeleteUserValue()
	{
		return fldDeleteUsername.getText();
	}
	
	public void clearFldDeleteUsername()
	{
		fldDeleteUsername.setText(null);
	}
	
	//public methods
}
