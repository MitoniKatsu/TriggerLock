/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This primary class contains the main window, and the application method.
 */
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
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

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
	protected static AddRangeVisitWindow addrangeVisitWindow;
	protected static RentalGunChooser rentalGunChooser;
	protected static AmmoChooser ammoChooser;
	protected static AddGunWindow addGunWindow;
	protected static ListGunsWindow listGunsWindow;
	protected static EditGunWindow editGunWindow;
	protected static ReturnGunWindow returnGunWindow;
	protected static AddAmmoWindow addAmmoWindow;
	protected static ListAmmoWindow listAmmoWindow;
	protected static EditAmmoWindow editAmmoWindow;
	protected static AddCaliberWindow addCaliberWindow;
	protected static ListCaliberWindow listCaliberWindow;
	protected static EditCaliberWindow editCaliberWindow;
	protected static AddManufacturerWindow addManufacturerWindow;
	protected static ListManufacturersWindow listManufacturersWindow;
	protected static EditManufacturerWindow editManufacturerWindow;
	protected static ServiceReportWindow serviceReportWindow;
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
		frmTriggerLock.setResizable(false);
		frmTriggerLock.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainApplication.class.getResource("/edu/seminolestate/mitoni/icon.png")));
		frmTriggerLock.setPreferredSize(new Dimension(1024, 768));
		frmTriggerLock.setTitle("Trigger-Lock Management System");
		frmTriggerLock.setBounds(100, 100, 1024, 768);
		frmTriggerLock.getContentPane().setLayout(null);
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(new Color(204, 204, 204));
		desktopPane.setBounds(0, 0, 1018, 740);
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
		addrangeVisitWindow = new AddRangeVisitWindow();
		desktopPane.add(addrangeVisitWindow.frmAddRangeVisit);
		rentalGunChooser = new RentalGunChooser();
		desktopPane.add(rentalGunChooser.frmRentalGunChooser);
		ammoChooser = new AmmoChooser();
		desktopPane.add(ammoChooser.frmAmmoChooser);
		addGunWindow = new AddGunWindow();
		desktopPane.add(addGunWindow.frmAddGun);
		listGunsWindow = new ListGunsWindow();
		desktopPane.add(listGunsWindow.frmListGuns);
		editGunWindow = new EditGunWindow();
		desktopPane.add(editGunWindow.frmEditGun);
		returnGunWindow = new ReturnGunWindow();
		desktopPane.add(returnGunWindow.frmReturnGun);
		addAmmoWindow = new AddAmmoWindow();
		desktopPane.add(addAmmoWindow.frmAddAmmo);
		listAmmoWindow = new ListAmmoWindow();
		desktopPane.add(listAmmoWindow.frmListAmmo);
		editAmmoWindow = new EditAmmoWindow();
		desktopPane.add(editAmmoWindow.frmEditAmmo);
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
		serviceReportWindow = new ServiceReportWindow();
		desktopPane.add(serviceReportWindow.frmReport);
		
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(MainApplication.class.getResource("/edu/seminolestate/mitoni/rifle.png")));
		label.setBounds(172, 212, 653, 326);
		desktopPane.add(label);

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
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
