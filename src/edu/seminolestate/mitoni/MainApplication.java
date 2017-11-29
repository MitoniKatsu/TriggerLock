package edu.seminolestate.mitoni;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JDesktopPane;


import edu.seminolestate.mitoni.QueryRole.Role;

/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This primary class contains the main window, and the application method.
 */
public class MainApplication
{
	// global static variables
	private static String currentUser; // hold username
	private static char[] currentPassword; // hold password
	private static Role currentRole; // hold role
	
	// main window
	protected JFrame frmTriggerLock;
	
	// sub-windows
	private JDesktopPane desktopPane; //main pane for all windows
	protected static LoginWindow loginWindow;
	protected static AddUserWindow addUserWindow;
	protected static ListUserRoles listUserRoles;
	protected static EditUserRole editUserRole;
	protected static DeleteUserWindow deleteUserWindow;
	protected static AddCustomerWindow addCustomerWindow;
	protected static CustomerSearchWindow customerSearchWindow;
	protected static EditCustomerWindow editCustomerWindow;
	protected static AddCaliberWindow addCaliberWindow;
	protected static ListCaliberWindow listCaliberWindow;
	protected static EditCaliberWindow editCaliberWindow;
	protected static AddManufacturerWindow addManufacturerWindow;
	protected static ListManufacturersWindow listManufacturersWindow;
	protected static EditManufacturerWindow editManufacturerWindow;
	private MainMenuBar mainMenu;


	// Getters/Setters
	public static String getCurrentUser()
	{
		return currentUser;
	}

	public static void setCurrentUser(String user)
	{
		currentUser = user;
	}

	public static String getCurrentPassword()
	{
		String stringPassword = "";

		for (int i = 0; i < currentPassword.length; i++)
		{
			stringPassword += currentPassword[i];
		}

		return stringPassword;
	}

	public static void setCurrentPassword(char[] password)
	{
		currentPassword = password;
	}

	public static Role getCurrentRole()
	{
		return currentRole;
	}

	public static void setCurrentRole(Role role)
	{
		currentRole = role;
	}
	
	public void setWindowEnabled(boolean bool)
	{
		
	}
	
	
	
	// create the main window
	public MainApplication()
	{
		initialize();
	}

	// initialize all window contents
	private void initialize()
	{
		// Main JFrame
		frmTriggerLock = new JFrame();
		frmTriggerLock.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainApplication.class.getResource("/edu/seminolestate/mitoni/icon.png")));
		frmTriggerLock.setPreferredSize(new Dimension(1024, 768));
		frmTriggerLock.setTitle("Trigger-Lock Management System");
		frmTriggerLock.setBounds(100, 100, 1024, 768);
		frmTriggerLock.getContentPane().setLayout(null);
		desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 1008, 709);
		frmTriggerLock.getContentPane().add(desktopPane);

		// create and add new sub-windows
		mainMenu = new MainMenuBar();
		mainMenu.setMenu(frmTriggerLock);
		loginWindow = new LoginWindow();
		desktopPane.add(loginWindow.frmLogin);
		addUserWindow = new AddUserWindow();
		desktopPane.add(addUserWindow.frmAddUser);
		listUserRoles = new ListUserRoles();
		desktopPane.add(listUserRoles.frmListUserRoles);
		editUserRole = new EditUserRole();
		desktopPane.add(editUserRole.frmEditUserRole);
		deleteUserWindow = new DeleteUserWindow();
		desktopPane.add(deleteUserWindow.frmDeleteUser);
		addCustomerWindow = new AddCustomerWindow();
		desktopPane.add(addCustomerWindow.frmAddCustomer);
		customerSearchWindow  = new CustomerSearchWindow();
		desktopPane.add(customerSearchWindow.frmCustomerSearch);
		editCustomerWindow = new EditCustomerWindow();
		desktopPane.add(editCustomerWindow.frmEditCustomer);
		addCaliberWindow = new AddCaliberWindow();
		desktopPane.add(addCaliberWindow.frmAddCaliber);
		listCaliberWindow = new ListCaliberWindow();
		desktopPane.add(listCaliberWindow.frmListCalibers);
		editCaliberWindow = new EditCaliberWindow();
		desktopPane.add(editCaliberWindow.frmEditCaliber);
		addManufacturerWindow = new AddManufacturerWindow();
		desktopPane.add(addManufacturerWindow.frmAddManufacturer);
		listManufacturersWindow = new ListManufacturersWindow();
		desktopPane.add(listManufacturersWindow.frmListManufacturers);
		editManufacturerWindow = new EditManufacturerWindow();
		desktopPane.add(editManufacturerWindow.frmEditManufacturer);

		// confirm close when "x" is clicked
		frmTriggerLock.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmTriggerLock.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
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

	
	}

	//Application	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainApplication window = new MainApplication();
					window.frmTriggerLock.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	

	

}