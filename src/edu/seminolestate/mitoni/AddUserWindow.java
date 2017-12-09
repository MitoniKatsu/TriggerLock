/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the add user window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import edu.seminolestate.mitoni.QueryRole.Role;

public class AddUserWindow
{
protected JInternalFrame frmAddUser;
	
	private JButton btnCancelCreateUser;
	private JButton btnCreateUser;	
	private JFormattedTextField fldUsername;
	private JFormattedTextField fldPassword;
	private JRadioButton radDBA;
	private JRadioButton radOwner;
	private JRadioButton radManager;
	private JRadioButton radRetail;
	
	public AddUserWindow()
	{
		//build window components
		frmAddUser = new JInternalFrame("Add User");
		frmAddUser.setBounds(200, 200, 246, 236);
		frmAddUser.getContentPane().setLayout(null);

		JLabel lblAddUsername = new JLabel("Username");
		lblAddUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddUsername.setBounds(10, 11, 60, 14);
		frmAddUser.getContentPane().add(lblAddUsername);

		JLabel lblAddPassword = new JLabel("Password");
		lblAddPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddPassword.setBounds(10, 36, 60, 14);
		frmAddUser.getContentPane().add(lblAddPassword);

		fldUsername = new JFormattedTextField();
		fldUsername.setBounds(80, 8, 100, 20);
		frmAddUser.getContentPane().add(fldUsername);

		fldPassword = new JFormattedTextField();
		fldPassword.setBounds(80, 33, 100, 20);
		frmAddUser.getContentPane().add(fldPassword);		
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRole.setBounds(24, 61, 46, 14);
		frmAddUser.getContentPane().add(lblRole);

		radRetail = new JRadioButton("Retail");
		radRetail.setSelected(true);
		radRetail.setBounds(80, 57, 109, 23);
		frmAddUser.getContentPane().add(radRetail);

		radManager = new JRadioButton("Manager");
		radManager.setBounds(80, 83, 109, 23);
		frmAddUser.getContentPane().add(radManager);

		radOwner = new JRadioButton("Owner");
		radOwner.setBounds(80, 109, 109, 23);
		frmAddUser.getContentPane().add(radOwner);

		radDBA = new JRadioButton("DBA");
		radDBA.setBounds(80, 135, 109, 23);
		frmAddUser.getContentPane().add(radDBA);

		ButtonGroup grpAddRole = new ButtonGroup();
		grpAddRole.add(radRetail);
		grpAddRole.add(radManager);
		grpAddRole.add(radOwner);
		grpAddRole.add(radDBA);
		
		btnCreateUser = new JButton("Create");
		frmAddUser.getRootPane().setDefaultButton(btnCreateUser);		
		btnCreateUser.setBounds(21, 168, 89, 23);
		frmAddUser.getContentPane().add(btnCreateUser);

		btnCancelCreateUser = new JButton("Cancel");
		btnCancelCreateUser.setBounds(120, 168, 89, 23);
		frmAddUser.getContentPane().add(btnCancelCreateUser);
		
		//Event handlers
		
		btnCreateUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Role newRole = Role.RETAIL; // default radio box selected

				// if changed to select another role other than default
				switch (checkRadGroup())
				{
				case 2:
					newRole = Role.MANAGER;
					break;
				case 3:
					newRole = Role.OWNER;
					break;
				case 4:
					newRole = Role.DBA;
					break;
				default:
					break;
				}
				// create new user to pass to user query
				User newUser = new User(getFldUsername(), getFldPassword(), newRole);
				// create new user query
				UserQuery addUser = new UserQuery(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				addUser.createUser(newUser);
				clearFldUsername();
				clearFldPassword();
				clearRadGroup();
				frmAddUser.setVisible(false);
			}
		});
		
		btnCancelCreateUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clearFldUsername();
				clearFldPassword();
				clearRadGroup();
				frmAddUser.setVisible(false);
			}
		});
	}
	//Getters-Setters
	public String getFldUsername()
	{
		return fldUsername.getText();
	}
	
	public String getFldPassword()
	{
		return fldPassword.getText();
	}
	
	public void clearFldUsername()
	{
		fldUsername.setText(null);
	}
	
	public void clearFldPassword()
	{
		fldPassword.setText(null);
	}
	
	public void clearRadGroup()
	{
		radRetail.setSelected(true);
	}
	
	//public methods
	
	public int checkRadGroup()
	{
		int radIndex = 1;
		
		if (radManager.isSelected())
		{
			radIndex = 2;
		}
		else if (radOwner.isSelected())
		{
			radIndex = 3;
		}
		else if (radDBA.isSelected())
		{
			radIndex = 4;
		}
		
		return radIndex;
	}
}
