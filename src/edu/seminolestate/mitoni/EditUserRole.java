/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the Edit User Role Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import edu.seminolestate.mitoni.QueryRole.Role;

public class EditUserRole
{
	protected JInternalFrame frmEditUserRole;
	
	private JButton btnCancelEditUserRole;
	private JButton btnUpdateUserRole;
	private JRadioButton radDBA;
	private JRadioButton radOwner;
	private JRadioButton radManager;
	private JRadioButton radRetail;
	private JLabel lblUsername;
	private String user = "";
	private String oldRole = "";

	public EditUserRole()
	{
		//build window components
		frmEditUserRole = new JInternalFrame("Edit User Role");
		frmEditUserRole.setBounds(200, 200, 246, 236);
		frmEditUserRole.getContentPane().setLayout(null);
	
		lblUsername = new JLabel("Username: ");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setBounds(24, 21, 182, 14);
		frmEditUserRole.getContentPane().add(lblUsername);
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRole.setBounds(24, 61, 46, 14);
		frmEditUserRole.getContentPane().add(lblRole);
	
		radRetail = new JRadioButton("Retail");
		radRetail.setSelected(true);
		radRetail.setBounds(80, 57, 109, 23);
		frmEditUserRole.getContentPane().add(radRetail);
	
		radManager = new JRadioButton("Manager");
		radManager.setBounds(80, 83, 109, 23);
		frmEditUserRole.getContentPane().add(radManager);
	
		radOwner = new JRadioButton("Owner");
		radOwner.setBounds(80, 109, 109, 23);
		frmEditUserRole.getContentPane().add(radOwner);
	
		radDBA = new JRadioButton("DBA");
		radDBA.setBounds(80, 135, 109, 23);
		frmEditUserRole.getContentPane().add(radDBA);
	
		ButtonGroup grpAddRole = new ButtonGroup();
		grpAddRole.add(radRetail);
		grpAddRole.add(radManager);
		grpAddRole.add(radOwner);
		grpAddRole.add(radDBA);
		
		btnUpdateUserRole = new JButton("Save Changes");
		frmEditUserRole.getRootPane().setDefaultButton(btnUpdateUserRole);		
		btnUpdateUserRole.setBounds(103, 173, 117, 23);
		frmEditUserRole.getContentPane().add(btnUpdateUserRole);
	
		btnCancelEditUserRole = new JButton("Cancel");
		btnCancelEditUserRole.setBounds(10, 173, 89, 23);
		frmEditUserRole.getContentPane().add(btnCancelEditUserRole);
		
		//Event handlers
		
		btnUpdateUserRole.addActionListener(new ActionListener()
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
				// create user query
				String revokeQuery = "REVOKE " + oldRole + " FROM " + user;
				String grantQuery = "GRANT " + newRole.toString() + " TO " + user;
				// create new user query
				UserQuery update = new UserQuery(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				update.updateUser(revokeQuery, grantQuery);
				clear();
			}
		});
		
		btnCancelEditUserRole.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clear();
			}
		});

	}
	
	//Getters-Setters
	public void clear()
	{
		lblUsername.setText("Username:");
		radRetail.setSelected(true);
		frmEditUserRole.setVisible(false);
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
	
	public void parseLoadUserRole(ResultSet rs)
	{		
		try
		{
			rs.next();
			//User Name			
			lblUsername.setText("Username: " + rs.getString(1));
			user = rs.getString(1);
			//User Role
			oldRole = rs.getString(2); //store old role
			
			switch (rs.getString(2)) //display old role in radio group
			{
			case "DBA":
				radDBA.setSelected(true);
				break;
			case "OWNER":
				radOwner.setSelected(true);
				break;
			case "MANAGER":
				radManager.setSelected(true);
				break;
			case "RETAIL":
				radRetail.setSelected(true);
				break;
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to parse result. Please try again, or see your system admin.");
		}
	}

}
