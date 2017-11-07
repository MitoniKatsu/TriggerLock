package edu.seminolestate.mitoni;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.seminolestate.mitoni.QueryRole.Role;



public class LoginWindow
{
	
	JInternalFrame frmLogin;
	JButton btnLoginCancel;
	JButton btnLoginSubmit;	
	
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
	
	//Public Methods
	public Role loginAndVerify(String user, String pass)
	{
		Role userRole;
		QueryRole newConnection = new QueryRole(user, pass, null);
		userRole = newConnection.CheckLogin();
		return userRole;
	}

}
