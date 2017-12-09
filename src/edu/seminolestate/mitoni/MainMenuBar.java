/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the main menu bar, and related methods and event handlers
 */
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

public class MainMenuBar
{
	protected static JMenuBar menuBar;
	// File Menu
	private static JMenu menuFile;
	private static JMenuItem menuFileLogin;
	private static JMenuItem menuFileLogout;
	private static JMenuItem menuCustomersSearch;
	private static JMenuItem menuFileManageAdd;
	private static JMenuItem menuFileManageDelete;
	private static JMenuItem menuFileManageEdit;
	private static JSeparator separator;
	private static JMenuItem menuFileExit;
	private static JMenuItem menuCustomerAddCustomer;
	private static JMenu menuHelp;
	private static JMenuItem menuHelpAbout;
	private static JMenu menuCustomer;
	private static JMenu menuGuns;
	private static JMenuItem menuGunsAdd;
	private static JMenuItem menuGunsEditOrDelete;
	private static JMenuItem menuGunsReturn;
	private static JMenu menuAmmo;
	private static JMenuItem menuAmmoAdd;
	private static JMenuItem menuAmmoEditOrDelete;
	private static JMenu menuManage;
	private static JMenu menuManageCaliber;
	private static JMenuItem menuManageCaliberAdd;
	private static JMenuItem menuManageCaliberEditOrDelete;
	private static JMenu menuManageManufacturers;
	private static JMenuItem menuManageManufacturersAdd;
	private static JMenuItem menuManageManufacturersEditOrDelete;
	private static JMenu menuReports;
	private static JMenuItem menuReportsServiceContact;
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

		menuFileManageEdit = new JMenuItem("List/Edit User Role");
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
		
		menuGuns = new JMenu("Guns");
		menuGuns.setVisible(false);
		menuBar.add(menuGuns);
		
		menuGunsAdd = new JMenuItem("Add New");
		menuGuns.add(menuGunsAdd);
		
		menuGunsEditOrDelete = new JMenuItem("Edit/Delete");
		menuGuns.add(menuGunsEditOrDelete);
		
		menuGunsReturn = new JMenuItem("Return from Range/Cleaning");
		menuGuns.add(menuGunsReturn);
		
		menuAmmo = new JMenu("Ammo");
		menuAmmo.setVisible(false);
		menuBar.add(menuAmmo);
		
		menuAmmoAdd = new JMenuItem("Add New");
		menuAmmo.add(menuAmmoAdd);
		
		menuAmmoEditOrDelete = new JMenuItem("Edit/Delete");
		menuAmmo.add(menuAmmoEditOrDelete);
		
		menuManage = new JMenu("Manage");
		menuManage.setMnemonic('M');
		menuManage.setVisible(false);
		menuBar.add(menuManage);
		
		menuManageCaliber = new JMenu("Caliber List");
		menuManage.add(menuManageCaliber);
		
		menuManageCaliberAdd = new JMenuItem("Add");
		menuManageCaliberAdd.setMnemonic('C');
		menuManageCaliber.add(menuManageCaliberAdd);
		
		menuManageCaliberEditOrDelete = new JMenuItem("Edit/Delete");
		menuManageCaliberEditOrDelete.setMnemonic('D');
		menuManageCaliber.add(menuManageCaliberEditOrDelete);
		
		menuManageManufacturers = new JMenu("Manufacturer List");
		menuManage.add(menuManageManufacturers);
		
		menuManageManufacturersAdd = new JMenuItem("Add");
		menuManageManufacturersAdd.setMnemonic('A');
		menuManageManufacturers.add(menuManageManufacturersAdd);
		
		menuManageManufacturersEditOrDelete = new JMenuItem("Edit/Delete");
		menuManageManufacturersEditOrDelete.setMnemonic('E');
		menuManageManufacturers.add(menuManageManufacturersEditOrDelete);
		
		menuReports = new JMenu("Reports");
		menuReports.setVisible(false);
		menuBar.add(menuReports);
		
		menuReportsServiceContact = new JMenuItem("Service Contacts");
		menuReports.add(menuReportsServiceContact);

		menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);

		menuHelpAbout = new JMenuItem("About");
		menuHelp.add(menuHelpAbout);
		
		//Event handlers
		menuFileLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainApplication.loginWindow.frmLogin.setVisible(true);
			}
		});
		
		menuFileManageAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainApplication.addUserWindow.frmAddUser.setVisible(true);
			}
		});
		
		menuFileManageDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainApplication.deleteUserWindow.frmDeleteUser.setVisible(true);
			}
		});
		
		menuFileManageEdit.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.listUserRoles.frmListUserRoles.setVisible(true);
				
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
				MainApplication.customerSearchWindow.frmCustomerSearch.setVisible(true);
			}
		});
		
		menuCustomerAddCustomer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.addCustomerWindow.frmAddCustomer.setVisible(true);
			}
		});
		
		menuGunsAdd.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.addGunWindow.frmAddGun.setVisible(true);
			}
		});
		
		menuGunsEditOrDelete.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.listGunsWindow.frmListGuns.setVisible(true);
			}
		});
		
		menuGunsReturn.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.returnGunWindow.frmReturnGun.setVisible(true);
			}
		});
		
		menuAmmoAdd.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.addAmmoWindow.frmAddAmmo.setVisible(true);
				
			}
		});
		
		menuAmmoEditOrDelete.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.listAmmoWindow.frmListAmmo.setVisible(true);
			}
		});
		
		menuManageCaliberAdd.addActionListener(new ActionListener()
		{
			

			public void actionPerformed(ActionEvent e)
			{
				MainApplication.addCaliberWindow.frmAddCaliber.setVisible(true);
			}
		});
		
		menuManageCaliberEditOrDelete.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.listCaliberWindow.frmListCalibers.setVisible(true);
			}
		});
		
		menuManageManufacturersAdd.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.addManufacturerWindow.frmAddManufacturer.setVisible(true);
			}
		});
		
		menuManageManufacturersEditOrDelete.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.listManufacturersWindow.frmListManufacturers.setVisible(true);				
			}
		});
		
		menuReportsServiceContact.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				MainApplication.serviceReportWindow.frmReport.setVisible(true);
			}
		});
		
		menuHelpAbout.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "The Trigger-Lock Range Management System is designed by Christian Lundblad for Seminole State College of Florida.");
				
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
	
	public static void setMenuGunsVisibility(boolean bool)
	{
		menuGuns.setVisible(bool);
	}
	
	public static void setMenuAmmoVisibility(boolean bool)
	{
		menuAmmo.setVisible(bool);
	}
	
	public static void setMenuManageVisibility(boolean bool)
	{
		menuManage.setVisible(bool);
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
		menuGuns.setVisible(false);
		menuAmmo.setVisible(false);
		menuManage.setVisible(false);
		menuReports.setVisible(false);
		MainApplication.setCurrentUser(null);
		MainApplication.setCurrentPassword(null);
		MainApplication.setCurrentRole(null);
	}
		
}
