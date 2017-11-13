package edu.seminolestate.mitoni;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.seminolestate.mitoni.QueryRole.Role;


/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the login window
 */
public class LoginWindow
{
	
	protected JInternalFrame frmLogin;
	
	private JButton btnLoginCancel;
	private JButton btnLoginSubmit;	
	private JTextField fldUsername;
	private JPasswordField pswPassword;
		
	public LoginWindow()
	{
		//build window components
		frmLogin = new JInternalFrame("Login");
		frmLogin.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		frmLogin.setBounds(150, 200, 400, 200);
		frmLogin.getContentPane().setLayout(null);		
		
		JLabel lblEnterLoginCredentials = new JLabel("Enter Login Credentials");
		lblEnterLoginCredentials.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblEnterLoginCredentials.setBounds(75, 11, 234, 35);
		frmLogin.getContentPane().add(lblEnterLoginCredentials);
		
		JLabel lblUsername = new JLabel("username: ");
		lblUsername.setBounds(85, 57, 66, 14);
		frmLogin.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("password: ");
		lblPassword.setBounds(85, 88, 66, 14);
		frmLogin.getContentPane().add(lblPassword);
		
		fldUsername = new JTextField();
		fldUsername.setBounds(161, 54, 119, 20);
		frmLogin.getContentPane().add(fldUsername);
		fldUsername.setColumns(10);
		
		pswPassword = new JPasswordField();
		pswPassword.setBounds(161, 85, 119, 20);
		frmLogin.getContentPane().add(pswPassword);
		
		btnLoginCancel = new JButton("cancel");
		btnLoginCancel.setBounds(50, 137, 89, 23);
		frmLogin.getContentPane().add(btnLoginCancel);
		
		btnLoginSubmit = new JButton("Submit");
		btnLoginSubmit.setBounds(240, 137, 89, 23);
		frmLogin.getContentPane().add(btnLoginSubmit);
		frmLogin.getRootPane().setDefaultButton(btnLoginSubmit);
		
		//Event Handlers
		btnLoginCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				clearUserFldValue();
				;
				clearPasswordFldValue();
				frmLogin.doDefaultCloseAction();
			}
		});
		
		btnLoginSubmit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MainWindow.setCurrentUser(getUserFldValue());
				MainWindow.setCurrentPassword(getPswPasswordValue());
				MainWindow.setCurrentRole(Role.INVALID); // default role

				switch (loginAndVerify(MainWindow.getCurrentUser(), MainWindow.getCurrentPassword())) 
				
				// verify  if login is valid, and if so, which role the user has
				{
				case DBA:
					verified(Role.DBA);
					MainMenuBar.setMenuFileLoginEnabled(false);
					MainMenuBar.setMenuFileLogoutEnabled(true);
					MainMenuBar.setMenuFileManageUsersVisibility(true);
					MainMenuBar.setSepManageUserVisibility(true);
					MainMenuBar.setMenuCustomerVisibility(true);
					MainMenuBar.setMenuReportsVisibility(true);

					break;
				case OWNER:
					verified(Role.OWNER);
					MainMenuBar.setMenuFileLoginEnabled(false);
					MainMenuBar.setMenuFileLogoutEnabled(true);
					MainMenuBar.setMenuFileManageUsersVisibility(true);
					MainMenuBar.setSepManageUserVisibility(true);
					MainMenuBar.setMenuCustomerVisibility(true);
					MainMenuBar.setMenuReportsVisibility(true);
					break;
				case MANAGER:
					verified(Role.MANAGER);
					MainMenuBar.setMenuFileLoginEnabled(false);
					MainMenuBar.setMenuFileLogoutEnabled(true);
					MainMenuBar.setMenuCustomerVisibility(true);
					MainMenuBar.setMenuReportsVisibility(true);
					break;
				case RETAIL:
					verified(Role.RETAIL);
					MainMenuBar.setMenuFileLoginEnabled(false);
					MainMenuBar.setMenuFileLogoutEnabled(true);
					MainMenuBar.setMenuCustomerVisibility(true);
					break;
				case INVALID:
				default:
					JOptionPane.showMessageDialog(null, "Invalid login, please try again.");
					break;
				}
			}
		});
	}
	
	//Getters-Setters
	public String getUserFldValue()
	{
		return fldUsername.getText();
	}

	public char[] getPswPasswordValue()
	{
		return pswPassword.getPassword();
	}
	
	public void clearUserFldValue()
	{
		fldUsername.setText(null);
	}
	
	public void clearPasswordFldValue()
	{
		pswPassword.setText(null);
	}
	
	public void frmLoginVisible(boolean vis)
	{
		frmLogin.setVisible(vis);
	}
	
	//Public Methods
	
	public Role loginAndVerify(String user, String pass)
	{
		Role userRole;
		QueryRole newConnection = new QueryRole(user, pass, null);
		userRole = newConnection.CheckLogin();
		return userRole;
	}
	
	public void verified(Role verifiedRole)
	{
		MainWindow.setCurrentRole(verifiedRole);
		clearUserFldValue();
		;
		clearPasswordFldValue();
		;
		frmLogin.setVisible(false);
	}

}
