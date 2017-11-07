package edu.seminolestate.mitoni;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import edu.seminolestate.mitoni.QueryRole.Role;

import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MainWindow
{
	// global field variables
	private String currentUser; // hold username
	private char[] currentPassword; // hold password
	private Role currentRole; // hold role

	// sub-windows
	private LoginWindow loginWindow;
	private AddUserWindow addUserWindow;
	private DeleteUserWindow deleteUserWindow;
	private AddCustomerWindow addCustomerWindow;

	// global swing components
	private JMenu menuFile;
	private JFrame frmTriggerLock;
	// File Menu
	private JMenuItem menuFileLogin;
	private JMenuItem menuFileLogout;
	private JMenuItem menuCustomersSearch;
	private JMenu menuCustomer;
	private JMenu menuReports;
	private JSeparator sepManageUser;
	private JMenu menuFileManageUsers;
	// Customer Search
	private JInternalFrame frmCustomerSearch;

	// array of us state abbreviations
	final String[] STATES = { "~~", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", "ID",
			"IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ",
			"NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV",
			"WY" };

	private JTable tblCustSearchResults;
	private JTextField fldCustSearchCustNum;

	// Get/Set
	public String getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser(String user)
	{
		this.currentUser = user;
	}

	public String getCurrentPassword()
	{
		String stringPassword = "";

		for (int i = 0; i < currentPassword.length; i++)
		{
			stringPassword += currentPassword[i];
		}

		return stringPassword;
	}

	public void setCurrentPassword(char[] password)
	{
		this.currentPassword = password;
	}

	public Role getCurrentRole()
	{
		return currentRole;
	}

	public void setCurrentRole(Role role)
	{
		this.currentRole = role;
	}

	// Application
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow window = new MainWindow();
					window.frmTriggerLock.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	// create the main window
	public MainWindow()
	{
		initialize();
	}

	// initialize all window contents
	private void initialize()
	{
		// Main JFrame
		frmTriggerLock = new JFrame();
		frmTriggerLock.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainWindow.class.getResource("/edu/seminolestate/mitoni/icon.png")));
		frmTriggerLock.setPreferredSize(new Dimension(1024, 768));
		frmTriggerLock.setTitle("Trigger-Lock Management System");
		frmTriggerLock.setBounds(100, 100, 1024, 768);
		frmTriggerLock.getContentPane().setLayout(null);
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(0, 0, 1008, 709);
		frmTriggerLock.getContentPane().add(desktopPane);

		// Add sub-windows
		loginWindow = new LoginWindow();
		desktopPane.add(loginWindow.frmLogin);
		addUserWindow = new AddUserWindow();
		desktopPane.add(addUserWindow.frmAddUser);
		deleteUserWindow = new DeleteUserWindow();
		desktopPane.add(deleteUserWindow.frmDeleteUser);
		addCustomerWindow = new AddCustomerWindow();
		desktopPane.add(addCustomerWindow.frmAddCustomer);

		//// Event Handlers////

		// login window
		loginWindow.btnLoginCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				loginWindow.clearUserFldValue();
				;
				loginWindow.clearPasswordFldValue();
				loginWindow.frmLogin.doDefaultCloseAction();
			}
		});

		loginWindow.btnLoginSubmit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setCurrentUser(loginWindow.getUserFldValue());
				setCurrentPassword(loginWindow.getPswPasswordValue());
				setCurrentRole(Role.INVALID); // default role

				switch (loginWindow.loginAndVerify(getCurrentUser(), getCurrentPassword())) // verify
																							// if
																							// login
																							// is
																							// valid,
																							// and
																							// if
				// so, which role the user has
				{
				case DBA:
					verified(Role.DBA);
					menuFileLogin.setEnabled(false);
					menuFileLogout.setEnabled(true);
					menuFileManageUsers.setVisible(true);
					sepManageUser.setVisible(true);
					menuCustomer.setVisible(true);
					menuReports.setVisible(true);

					break;
				case OWNER:
					verified(Role.OWNER);
					menuFileLogin.setEnabled(false);
					menuFileLogout.setEnabled(true);
					menuFileManageUsers.setVisible(true);
					sepManageUser.setVisible(true);
					menuCustomer.setVisible(true);
					menuReports.setVisible(true);
					break;
				case MANAGER:
					verified(Role.MANAGER);
					menuFileLogin.setEnabled(false);
					menuFileLogout.setEnabled(true);
					menuCustomer.setVisible(true);
					menuReports.setVisible(true);
					break;
				case RETAIL:
					verified(Role.RETAIL);
					menuFileLogin.setEnabled(false);
					menuFileLogout.setEnabled(true);
					menuCustomer.setVisible(true);
					break;
				case INVALID:
				default:
					JOptionPane.showMessageDialog(frmTriggerLock, "Invalid login, please try again.");
					break;
				}
			}
		});
		// add user window
		addUserWindow.btnCreateUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Role newRole = Role.RETAIL; // default radio box selected

				// if changed to select another role other than default
				switch (addUserWindow.checkRadGroup())
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
				User newUser = new User(addUserWindow.getFldUsername(), addUserWindow.getFldPassword(), newRole);
				// create new user query
				UserQuery addUser = new UserQuery(getCurrentUser(), getCurrentPassword(), null);
				addUser.createUser(newUser);
				addUserWindow.clearFldUsername();
				addUserWindow.clearFldPassword();
				addUserWindow.clearRadGroup();
				addUserWindow.frmAddUser.setVisible(false);
			}
		});

		addUserWindow.btnCancelCreateUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addUserWindow.clearFldUsername();
				addUserWindow.clearFldPassword();
				addUserWindow.clearRadGroup();
				addUserWindow.frmAddUser.setVisible(false);
			}
		});

		// delete user window
		deleteUserWindow.btnDeleteUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				UserQuery deleteUser = new UserQuery(getCurrentUser(), getCurrentPassword(), null);
				deleteUser.dropUser(deleteUserWindow.getfldDeleteUserValue());
				deleteUserWindow.clearFldDeleteUsername();
				deleteUserWindow.frmDeleteUser.setVisible(false);
			}
		});

		deleteUserWindow.btnCancelDeleteUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				deleteUserWindow.clearFldDeleteUsername();
				deleteUserWindow.frmDeleteUser.setVisible(false);
			}
		});

		// add customer window
		addCustomerWindow.btnAddCustSubmit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				QueryCustomer addNew = new QueryCustomer(getCurrentUser(), getCurrentPassword(), null);
				if (addCustomerWindow.validateAddCustomer() != null)
				{
					addNew.addCustomer(addCustomerWindow.validateAddCustomer());
					addCustomerWindow.clearAddCustomer();
				}
			}
		});

		addCustomerWindow.btnAddCustCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addCustomerWindow.clearAddCustomer();
			}
		});

		// Customer search window
		frmCustomerSearch = new JInternalFrame("Customer Search");
		frmCustomerSearch.setBounds(78, 66, 900, 400);
		desktopPane.add(frmCustomerSearch);
		frmCustomerSearch.getContentPane().setLayout(null);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setBounds(0, 0, 884, 371);
		frmCustomerSearch.getContentPane().add(splitPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabbedPane);

		JPanel pnlCustSearchCustNum = new JPanel();
		tabbedPane.addTab("Customer #", null, pnlCustSearchCustNum, null);
		pnlCustSearchCustNum.setLayout(null);

		JLabel lblCustSearchCustNum = new JLabel("Customer #");
		lblCustSearchCustNum.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCustSearchCustNum.setBounds(21, 11, 79, 14);
		pnlCustSearchCustNum.add(lblCustSearchCustNum);

		fldCustSearchCustNum = new JTextField();
		fldCustSearchCustNum.setHorizontalAlignment(SwingConstants.TRAILING);
		fldCustSearchCustNum.setDocument(new FieldLimit(6));
		fldCustSearchCustNum.setBounds(110, 8, 86, 20);
		pnlCustSearchCustNum.add(fldCustSearchCustNum);
		fldCustSearchCustNum.setColumns(10);

		JButton btnCustSearchCustNumSearch = new JButton("Search");
		btnCustSearchCustNumSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				QueryCustomer newSearch = new QueryCustomer(getCurrentUser(), getCurrentPassword(), null);
				if (!fldCustSearchCustNum.getText().trim().isEmpty())
				{
					try
					{
						int custNum;

						custNum = Integer.parseInt(fldCustSearchCustNum.getText());
						try
						{
							tblCustSearchResults.setModel(buildTableModel(newSearch.searchCustomerNumber(custNum)));
						}
						catch (SQLException e)
						{
							JOptionPane.showMessageDialog(null, "Please enter a valid customer number");
						}
					}
					catch (NumberFormatException ex)
					{
						JOptionPane.showMessageDialog(null, "Please enter a valid customer number");
					}
					finally
					{
						newSearch.closeSQL();
					}

				}
				else
				{
					try
					{
						tblCustSearchResults.setModel(buildTableModel(newSearch.searchCustomerNumber(0)));
						for (int i = 0; i > tblCustSearchResults.getColumnCount(); i++)
						{
							for (int j = 0; j > tblCustSearchResults.getRowCount(); j++)
							{

							}
						}
					}
					catch (SQLException e)
					{
						JOptionPane.showMessageDialog(null, "Please enter a valid customer number");
						e.printStackTrace();
					}
					finally
					{
						newSearch.closeSQL();
					}
				}
			}
		});
		btnCustSearchCustNumSearch.setBounds(778, 7, 89, 23);
		pnlCustSearchCustNum.add(btnCustSearchCustNumSearch);

		JButton bynCustSearchCustNumCancel = new JButton("Cancel");
		bynCustSearchCustNumCancel.setBounds(679, 7, 89, 23);
		pnlCustSearchCustNum.add(bynCustSearchCustNumCancel);

		JPanel pnlCustSearchCustName = new JPanel();
		tabbedPane.addTab("Customer Name", null, pnlCustSearchCustName, null);
		pnlCustSearchCustName.setLayout(null);

		JPanel pnlCustSearchCustPhone = new JPanel();
		tabbedPane.addTab("Phone", null, pnlCustSearchCustPhone, null);
		pnlCustSearchCustPhone.setLayout(null);

		JScrollPane scrlPnCustSearchResults = new JScrollPane();
		scrlPnCustSearchResults.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setRightComponent(scrlPnCustSearchResults);

		tblCustSearchResults = new JTable();
		scrlPnCustSearchResults.setViewportView(tblCustSearchResults);
		splitPane.setDividerLocation(65);

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

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		frmTriggerLock.setJMenuBar(menuBar);

		menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);

		menuFileLogin = new JMenuItem("Login");
		menuFileLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				loginWindow.frmLogin.setVisible(true);
			}
		});

		sepManageUser = new JSeparator();
		sepManageUser.setVisible(false);

		menuFileManageUsers = new JMenu("Manage Users");
		menuFileManageUsers.setVisible(false);
		menuFile.add(menuFileManageUsers);

		JMenuItem menuFileManageAdd = new JMenuItem("Add User");
		menuFileManageAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addUserWindow.frmAddUser.setVisible(true);
			}
		});
		menuFileManageUsers.add(menuFileManageAdd);

		JMenuItem menuFileManageDelete = new JMenuItem("Delete User");
		menuFileManageDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				deleteUserWindow.frmDeleteUser.setVisible(true);
			}
		});
		menuFileManageUsers.add(menuFileManageDelete);

		JMenuItem menuFileManageEdit = new JMenuItem("Edit User Role");
		menuFileManageUsers.add(menuFileManageEdit);
		menuFile.add(sepManageUser);
		menuFileLogin.setMnemonic('N');
		menuFile.add(menuFileLogin);

		menuFileLogout = new JMenuItem("Logout");
		menuFileLogout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				lockDown();
			}
		});
		menuFileLogout.setMnemonic('O');
		menuFileLogout.setEnabled(false);
		menuFile.add(menuFileLogout);

		JSeparator separator = new JSeparator();
		menuFile.add(separator);

		JMenuItem menuFileExit = new JMenuItem("Exit");
		menuFileExit.setMnemonic('X');
		menuFile.add(menuFileExit);
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

		menuCustomer = new JMenu("Customer");
		menuCustomer.setVisible(false);
		menuBar.add(menuCustomer);

		menuCustomersSearch = new JMenuItem("Search");
		menuCustomersSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				frmCustomerSearch.setVisible(true);
			}
		});
		menuCustomer.add(menuCustomersSearch);

		JMenuItem mntmAddCustomer = new JMenuItem("Add Customer");
		mntmAddCustomer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				addCustomerWindow.frmAddCustomer.setVisible(true);
			}
		});
		menuCustomer.add(mntmAddCustomer);

		menuReports = new JMenu("Reports");
		menuReports.setVisible(false);
		menuBar.add(menuReports);

		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);

		JMenuItem menuHelpAbout = new JMenuItem("About");
		menuHelp.add(menuHelpAbout);
	}

	private void verified(Role verifiedRole)
	{
		setCurrentRole(verifiedRole);
		loginWindow.clearUserFldValue();
		;
		loginWindow.clearPasswordFldValue();
		;
		loginWindow.frmLogin.setVisible(false);
	}

	// on logout, lockdown functions
	private void lockDown()
	{
		menuFileLogin.setEnabled(true);
		menuFileLogout.setEnabled(false);
		menuFileManageUsers.setVisible(false);
		sepManageUser.setVisible(false);
		menuCustomer.setVisible(false);
		menuReports.setVisible(false);
		setCurrentUser(null);
		setCurrentPassword(null);
		setCurrentRole(null);
	}

	public static TableModel buildTableModel(ResultSet rs) throws SQLException
	{
		// get metadata of results
		ResultSetMetaData rsmd = rs.getMetaData();

		// populate column names
		Vector<String> columns = new Vector<String>();
		int countColumns = rsmd.getColumnCount();
		for (int col = 1; col <= countColumns; col++)
		{
			columns.add(rsmd.getColumnName(col));
		}

		// populate rows
		Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		while (rs.next())
		{
			Vector<Object> vector = new Vector<Object>();
			for (int colIndex = 1; colIndex <= countColumns; colIndex++)
			{
				vector.add(rs.getObject(colIndex));
			}
			tableData.add(vector);
		}
		
		return new DefaultTableModel(tableData, columns)
		{

			private static final long serialVersionUID = 1L;
			//lock table contents from user editing
			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex)
			{
				return false;
			}
		};
	}

}
