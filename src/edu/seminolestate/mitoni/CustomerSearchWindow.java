/* 
 * Written by Christian Lundblad
 * November 11, 2017
 * This class contains the customer search window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class CustomerSearchWindow
{
	protected JInternalFrame frmCustomerSearch;
	private JButton btnCustSearchCustNumSearch;
	private JButton btnCustSearchNameCancel;
	private JButton btnCustSearchNameSearch;
	private JButton btnCustSearchCustNumCancel;
	private JButton btnCustSearchPhoneSearch;
	private JButton btnCustSearchPhoneCancel;
	private JMenuItem mntmAddRangeVisit;
	private JMenuItem mntmEditCustomer;
	private JMenuItem mntmDeleteCustomer;
	private DefaultTableModel model;
	
	private JTable tblResults;
	private JTextField fldCustNum;
	private JTextField fldFirst;
	private JTextField fldLast;
	private JTextField fldPhone;


		
	
	public CustomerSearchWindow()
	{
		//build window components
		frmCustomerSearch = new JInternalFrame("Customer Search");
		frmCustomerSearch.setBounds(46, 67, 900, 400);
		frmCustomerSearch.getContentPane().setLayout(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setBounds(0, 0, 884, 371);
		frmCustomerSearch.getContentPane().add(splitPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabbedPane);

		JPanel pnlCustNum = new JPanel();
		tabbedPane.addTab("Customer #", null, pnlCustNum, null);
		pnlCustNum.setLayout(null);

		JLabel lblCustNum = new JLabel("Customer #");
		lblCustNum.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCustNum.setBounds(10, 11, 71, 14);
		pnlCustNum.add(lblCustNum);

		fldCustNum = new JTextField();
		fldCustNum.setDocument(new FieldLimit(6));
		fldCustNum.setBounds(91, 8, 86, 20);
		pnlCustNum.add(fldCustNum);
		fldCustNum.setColumns(10);
		
		btnCustSearchCustNumSearch = new JButton("Search");
		btnCustSearchCustNumSearch.setBounds(778, 7, 89, 23);
		pnlCustNum.add(btnCustSearchCustNumSearch);
		
		btnCustSearchCustNumCancel = new JButton("Cancel");
		btnCustSearchCustNumCancel.setBounds(679, 7, 89, 23);
		pnlCustNum.add(btnCustSearchCustNumCancel);
		
		JPanel pnlCustName = new JPanel();
		tabbedPane.addTab("Customer Name", null, pnlCustName, null);
		pnlCustName.setLayout(null);
		
		JLabel lblFirst = new JLabel("First Name");
		lblFirst.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFirst.setBounds(10, 11, 67, 14);
		pnlCustName.add(lblFirst);
		
		JLabel lblLast = new JLabel("Last Name");
		lblLast.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLast.setBounds(187, 11, 67, 14);
		pnlCustName.add(lblLast);

		fldFirst = new JTextField();
		fldFirst.setBounds(87, 8, 90, 20);
		pnlCustName.add(fldFirst);
		fldFirst.setColumns(10);
		fldFirst.setDocument(new FieldLimit(12));

		fldLast = new JTextField();
		fldLast.setColumns(10);
		fldLast.setBounds(264, 8, 90, 20);
		pnlCustName.add(fldLast);
		fldLast.setDocument(new FieldLimit(12));

		btnCustSearchNameCancel = new JButton("Cancel");
		btnCustSearchNameCancel.setBounds(679, 7, 89, 23);
		pnlCustName.add(btnCustSearchNameCancel);
		
		btnCustSearchNameSearch = new JButton("Search");
		btnCustSearchNameSearch.setBounds(778, 7, 89, 23);
		pnlCustName.add(btnCustSearchNameSearch);
		
		JPanel pnlCustPhone = new JPanel();
		tabbedPane.addTab("Phone", null, pnlCustPhone, null);
		pnlCustPhone.setLayout(null);

		fldPhone = new JTextField();
		fldPhone.setColumns(10);
		fldPhone.setBounds(209, 8, 86, 20);
		pnlCustPhone.add(fldPhone);
		fldPhone.setDocument(new FieldLimit(12));

		JLabel lblPhone = new JLabel("Phone Number ( with area code )");
		lblPhone.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPhone.setBounds(10, 11, 189, 14);
		pnlCustPhone.add(lblPhone);

		btnCustSearchPhoneCancel = new JButton("Cancel");
		btnCustSearchPhoneCancel.setBounds(679, 7, 89, 23);
		pnlCustPhone.add(btnCustSearchPhoneCancel);
		
		btnCustSearchPhoneSearch = new JButton("Search");
		btnCustSearchPhoneSearch.setBounds(778, 7, 89, 23);
		pnlCustPhone.add(btnCustSearchPhoneSearch);
		
		JScrollPane scrlPnResults = new JScrollPane();
		scrlPnResults.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setRightComponent(scrlPnResults);

		JPopupMenu ctxtCustomerSearchResult = new JPopupMenu();

		mntmAddRangeVisit = new JMenuItem("Open New Range Visit");
		ctxtCustomerSearchResult.add(mntmAddRangeVisit);
		
		JSeparator ctxtSeparator = new JSeparator();
		ctxtCustomerSearchResult.add(ctxtSeparator);
		
		mntmEditCustomer = new JMenuItem("Edit Customer");
		ctxtCustomerSearchResult.add(mntmEditCustomer);

		tblResults = new JTable();
		addPopup(tblResults, ctxtCustomerSearchResult);

		mntmDeleteCustomer = new JMenuItem("Delete Customer");
		ctxtCustomerSearchResult.add(mntmDeleteCustomer);
		
		scrlPnResults.setViewportView(tblResults);
		splitPane.setDividerLocation(65);
		
		//Event Handlers
		
		mntmAddRangeVisit.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				String custNum =  (String) tblResults.getModel().getValueAt(tblResults.getSelectedRow(), 0);
				
				//pass to add range visit window
				MainApplication.addrangeVisitWindow.setCustomerNumber(custNum);
				//open add range visit window
				MainApplication.addrangeVisitWindow.frmAddRangeVisit.setVisible(true);
			}
		});
		
		mntmEditCustomer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String loadNum = (String) tblResults.getModel().getValueAt(tblResults.getSelectedRow(), 0);
				
				QueryCustomer newQuery = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				ResultSet loadRS = newQuery.loadEditCustomer(loadNum);
				//parse results to edit window
				MainApplication.editCustomerWindow.parseLoadCustomer(loadRS);
				//load edit window
				MainApplication.editCustomerWindow.frmEditCustomer.setVisible(true);
				newQuery.closeSQL();
				clear();				
			}
		});

		mntmDeleteCustomer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String rowID = (String) tblResults.getModel().getValueAt(tblResults.getSelectedRow(), 0);
				
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete customer number "+ rowID
						+"? This operation cannot be reversed!", "Confirm Delete", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION)
				{
					QueryCustomer newQuery = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
					newQuery.deleteCustomer(rowID);
					clear();
				}
				else
				{
					// Do nothing
				}
				
			}
		});
		
		btnCustSearchCustNumSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				QueryCustomer newSearch = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				if (!getFldCustNum().trim().isEmpty())
				{
					try
					{
						int custNum;

						custNum = Integer.parseInt(getFldCustNum());
						try
						{
							model = buildTableModel(newSearch.searchCustomerNumber(custNum));
							tblResults.setModel(model);
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
						model = buildTableModel(newSearch.searchCustomerNumber(0));
						tblResults.setModel(model);
						for (int i = 0; i > tblResults.getColumnCount(); i++)
						{
							for (int j = 0; j > tblResults.getRowCount(); j++)
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
		
		btnCustSearchCustNumCancel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				clear();
			}
		});
		
		btnCustSearchNameCancel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				clear();
			}
		});
	
		btnCustSearchNameSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				QueryCustomer newSearch = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);

				String custFirst = getFldFirst().trim();
				String custLast = getFldLast().trim();

				try
				{
					model = buildTableModel(newSearch.searchCustomerName(custFirst, custLast));
					tblResults.setModel(model);
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null, "Search failed. Please try again, or see your system admin.");
				}
				finally
				{
					newSearch.closeSQL();
				}
			}
		});

		btnCustSearchPhoneCancel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				clear();
			}
		});

		btnCustSearchPhoneSearch.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				QueryCustomer newSearch = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				long custPhone;

				if (!getFldPhone().trim().isEmpty())
				{
					Pattern p = Pattern.compile("(\\d{3}) ?-?(\\d{3}) ?-?(\\d{4})");
					Matcher m = p.matcher(getFldPhone());
					if (m.matches())
					{
						String longString = m.group(1) + m.group(2) + m.group(3);
						// parse string to an integer
						try
						{
							custPhone = Long.parseLong(longString);

							try
							{
								model = buildTableModel(newSearch.searchCustomerPhone(custPhone));
								tblResults.setModel(model);
							}
							catch (SQLException sqlEx)
							{
								JOptionPane.showMessageDialog(null,
										"Search failed. Please try again, or see your system admin.");
							}
						}
						catch (NumberFormatException ex)
						{
							JOptionPane.showMessageDialog(null,
									"Phone number should only include numbers, dashes, or spaces ie: 555 555-5555.");
						}
						finally
						{
							newSearch.closeSQL();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null,
								"Phone number should only include numbers, dashes, or spaces ie: 555 555-5555.");
					}
				}
				else
				{
					custPhone = 0;
					try
					{
						model = buildTableModel(newSearch.searchCustomerPhone(custPhone));
						tblResults.setModel(model);
					}
					catch (SQLException sqlEx)
					{
						JOptionPane.showMessageDialog(null,
								"Search failed. Please try again, or see your system admin.");
					}
				}
			}
		});
	}
	
	//Getters-Setters
	
	public String getFldCustNum()
	{
		return fldCustNum.getText();
	}
	
	public String getFldFirst()
	{
		return fldFirst.getText();
	}
	
	public String getFldLast()
	{
		return fldLast.getText();
	}
	
	public String getFldPhone()
	{
		return fldPhone.getText();
	}
	
	//public methods
	
	public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException
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

			// lock table contents from user editing
			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex)
			{
				return false;
			}
		};
	}

	public void clear()
	{
		fldCustNum.setText(null);
		fldFirst.setText(null);
		fldLast.setText(null);
		fldPhone.setText(null);
		frmCustomerSearch.setVisible(false);
		if (model != null)
		{
			model.setRowCount(0);
		}		
	}

	private void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e)
			{
				if (tblResults.getSelectedRow() != -1)
				{
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}

}
