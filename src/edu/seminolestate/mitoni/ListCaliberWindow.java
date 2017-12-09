/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the List Caliber Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;


import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class ListCaliberWindow
{
	protected JInternalFrame frmListCalibers;
	
	private DefaultTableModel model;	
	private JTable tblCalibers;
	private JMenuItem mntmEditCaliber;
	private JMenuItem mntmDeleteCaliber;
	
	
	public ListCaliberWindow()
	{
		//build window components
		frmListCalibers = new JInternalFrame("Caliber List");
		frmListCalibers.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmListCalibers.setClosable(true);
		frmListCalibers.setBounds(50, 50, 200, 400);
		frmListCalibers.getContentPane().setLayout(null);
			//context menu
		JPopupMenu ctxtCaliberList = new JPopupMenu();
		mntmEditCaliber = new  JMenuItem("Edit Selected");
		ctxtCaliberList.add(mntmEditCaliber);
		JSeparator ctxtSeparator = new JSeparator();
		ctxtCaliberList.add(ctxtSeparator);
		mntmDeleteCaliber = new JMenuItem("Delete Selected");
		ctxtCaliberList.add(mntmDeleteCaliber);
		
		tblCalibers = new JTable();
		tblCalibers.setBounds(new Rectangle(0, 0, 200, 400));
		addPopup(tblCalibers, ctxtCaliberList);
		
		JScrollPane scrlPnManf = new JScrollPane();
		scrlPnManf.setLocation(0, 0);
		scrlPnManf.setSize(184, 371);
		scrlPnManf.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmListCalibers.getContentPane().add(scrlPnManf);
		scrlPnManf.setViewportView(tblCalibers);		
		
		//event handlers
		frmListCalibers.addComponentListener(new ComponentListener()
		{
			
			public void componentShown(ComponentEvent e)
			{
				QueryCaliber newQuery = new QueryCaliber(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				
				try
				{
					model = buildTableModel(newQuery.listCalibers());
					tblCalibers.setModel(model);
				}
				catch (SQLException ex)
				{
					JOptionPane.showMessageDialog(null, "Unable to load Caliber List. Please see your system admin.");
				}
				finally 
				{
					newQuery.closeSQL();
				}
			}

			@Override
			public void componentHidden(ComponentEvent e)
			{
				model.setRowCount(0);
			}

			@Override
			public void componentMoved(ComponentEvent e)
			{

			}

			@Override
			public void componentResized(ComponentEvent e)
			{

			}
			
			
		});
		
		mntmEditCaliber.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String caliber = (String) tblCalibers.getModel().getValueAt(tblCalibers.getSelectedRow(), 0);
				
				QueryCaliber newQuery = new QueryCaliber(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				ResultSet loadRS = newQuery.loadEditCaliber(caliber);
				//parse results to edit window
				MainApplication.editCaliberWindow.parseLoadCaliber(loadRS);
				//load edit window
				MainApplication.editCaliberWindow.frmEditCaliber.setVisible(true);
				newQuery.closeSQL();
				frmListCalibers.setVisible(false);
			}
		});
		
		mntmDeleteCaliber.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				String caliber = (String) tblCalibers.getModel().getValueAt(tblCalibers.getSelectedRow(), 0);
				
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete "+ caliber
						+"? This operation cannot be reversed, and will fail if any gun and ammo records use this Caliber!", "Confirm Delete", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION)
				{
					QueryCaliber newQuery = new QueryCaliber(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
					newQuery.deleteCaliber(caliber);
					frmListCalibers.setVisible(false);
				}
				else
				{
					// Do nothing
				}
			}
		});
	}
	
	//public methods
	
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
				if (tblCalibers.getSelectedRow() != -1)
				{
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	
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

}
