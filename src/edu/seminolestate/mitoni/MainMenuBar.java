package edu.seminolestate.mitoni;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the main menu bar, and menu related event handlers
 */
public class MainMenuBar
{
	protected static JMenuBar menuBar;
	// File Menu
	private JMenu menuFile;
	private static JMenuItem menuFileLogin;
	private static JMenuItem menuFileLogout;
	private JMenuItem menuCustomersSearch;
	private JMenuItem menuFileManageAdd;
	private JMenuItem menuFileManageDelete;
	private JMenuItem menuFileManageEdit;
	private JSeparator separator;
	private JMenuItem menuFileExit;
	private JMenuItem menuCustomerAddCustomer;
	private JMenu menuHelp;
	private JMenuItem menuHelpAbout;
	private static JMenu menuCustomer;
	private static JMenu menuReports;
	private static JSeparator sepManageUser;
	private static JMenu menuFileManageUsers;

	public MainMenuBar()
	{
		//build menu
		menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		
		menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);
		
		menuFileLogin = new JMenuItem("Login");
		
		sepManageUser = new JSeparator();
		sepManageUser.setVisible(false);

		menuFileManageUsers = new JMenu("Manage Users");
		menuFileManageUsers.setVisible(false);
		menuFile.add(menuFileManageUsers);

		menuFileManageAdd = new JMenuItem("Add User");
		menuFileManageUsers.add(menuFileManageAdd);

		menuFileManageDelete = new JMenuItem("Delete User");
		menuFileManageUsers.add(menuFileManageDelete);

		menuFileManageEdit = new JMenuItem("Edit User Role");
		menuFileManageUsers.add(menuFileManageEdit);
		menuFile.add(sepManageUser);
		menuFileLogin.setMnemonic('N');
		menuFile.add(menuFileLogin);

		menuFileLogout = new JMenuItem("Logout");
		menuFileLogout.setMnemonic('O');
		menuFileLogout.setEnabled(false);
		menuFile.add(menuFileLogout);

		separator = new JSeparator();
		menuFile.add(separator);

		menuFileExit = new JMenuItem("Exit");
		menuFileExit.setMnemonic('X');
		menuFile.add(menuFileExit);
		
		menuCustomer = new JMenu("Customer");
		menuCustomer.setVisible(false);
		menuBar.add(menuCustomer);

		menuCustomersSearch = new JMenuItem("Search");
		menuCustomer.add(menuCustomersSearch);

		menuCustomerAddCustomer = new JMenuItem("Add Customer");
		menuCustomer.add(menuCustomerAddCustomer);
		menuReports = new JMenu("Reports");
		menuReports.setVisible(false);
		menuBar.add(menuReports);

		menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);

		menuHelpAbout = new JMenuItem("About");
		menuHelp.add(menuHelpAbout);
		
		//Event handlers
		menuFileLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainWindow.loginWindow.frmLogin.setVisible(true);
			}
		});
		
		menuFileManageAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainWindow.addUserWindow.frmAddUser.setVisible(true);
			}
		});
		
		menuFileManageDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainWindow.deleteUserWindow.frmDeleteUser.setVisible(true);
			}
		});
		
		menuFileLogout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				lockDown();
			}
		});
		
		menuFileExit.addActionListener(new ActionListener()
		{
			// confirm on close
			public void actionPerformed(ActionEvent arg0)
			{
				int result = JOptionPane.showConfirmDialog(null, "Are you sure", "Confirm", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
				else
				{
					// Do nothing
				}
			}
		});
		
		menuCustomersSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainWindow.customerSearchWindow.frmCustomerSearch.setVisible(true);
			}
		});
		
		menuCustomerAddCustomer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MainWindow.addCustomerWindow.frmAddCustomer.setVisible(true);
			}
		});
	
	}
	
	//Getters/Setters
	public void setMenu(JFrame frame)
	{
		frame.setJMenuBar(menuBar);
	}
	
	public static void setMenuFileLoginEnabled(boolean bool)
	{
		menuFileLogin.setEnabled(bool);
	}
	
	public static void setMenuFileLogoutEnabled(boolean bool)
	{
		menuFileLogout.setEnabled(bool);
	}
	
	public static void setMenuFileManageUsersVisibility(boolean bool)
	{
		menuFileManageUsers.setVisible(bool);
	}
	
	public static void setSepManageUserVisibility(boolean bool)
	{
		sepManageUser.setVisible(bool);
	}
	
	public static void setMenuCustomerVisibility(boolean bool)
	{
		menuCustomer.setVisible(bool);
	}
	
	public static void setMenuReportsVisibility(boolean bool)
	{
		menuReports.setVisible(bool);
	}
	
	//public methods
	// on logout, lockdown functions
	public static void lockDown()
	{
		menuFileLogin.setEnabled(true);
		menuFileLogout.setEnabled(false);
		menuFileManageUsers.setVisible(false);
		sepManageUser.setVisible(false);
		menuCustomer.setVisible(false);
		menuReports.setVisible(false);
		MainWindow.setCurrentUser(null);
		MainWindow.setCurrentPassword(null);
		MainWindow.setCurrentRole(null);
	}
		
}
