/*
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the List Guns Window, and related methods and event handlers
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

public class ListGunsWindow
{
	protected JInternalFrame frmListGuns;
	
	private DefaultTableModel model;
	private JTable tblGuns;
	private JMenuItem mntmEditGun;
	private JMenuItem mntmDeleteGun;

	public ListGunsWindow()
	{
		//build window components
		frmListGuns = new JInternalFrame("Guns List");
		frmListGuns.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmListGuns.setClosable(true);
		frmListGuns.setBounds(50, 50, 600, 400);
		frmListGuns.getContentPane().setLayout(null);
			//context menu
		JPopupMenu ctxtManufacturerList = new JPopupMenu();
		mntmEditGun = new  JMenuItem("Edit Selected");
		ctxtManufacturerList.add(mntmEditGun);
		JSeparator ctxtSeparator = new JSeparator();
		ctxtManufacturerList.add(ctxtSeparator);
		mntmDeleteGun = new JMenuItem("Delete Selected");
		ctxtManufacturerList.add(mntmDeleteGun);
		
		tblGuns = new JTable();
		tblGuns.setBounds(new Rectangle(0, 0, 200, 400));
		addPopup(tblGuns, ctxtManufacturerList);
		
		JScrollPane scrlPnManf = new JScrollPane();
		scrlPnManf.setLocation(0, 0);
		scrlPnManf.setSize(584, 371);
		scrlPnManf.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmListGuns.getContentPane().add(scrlPnManf);
		scrlPnManf.setViewportView(tblGuns);

		//event handlers
		frmListGuns.addComponentListener(new ComponentListener()
		{
			
			public void componentShown(ComponentEvent e)
			{
				QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				
				try
				{
					model = buildTableModel(newQuery.listGuns());
					tblGuns.setModel(model);
				}
				catch (SQLException ex)
				{
					JOptionPane.showMessageDialog(null, "Unable to load Ammo List. Please see your system admin.");
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
		
		mntmEditGun.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String serial = (String) tblGuns.getModel().getValueAt(tblGuns.getSelectedRow(), 0);
				
				QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				ResultSet loadRS = newQuery.loadEditGun(serial);
				//parse results to edit window
				MainApplication.editGunWindow.parseLoadGun(loadRS);
				//load edit window
				MainApplication.editGunWindow.frmEditGun.setVisible(true);
				newQuery.closeSQL();
				frmListGuns.setVisible(false);
			}

		});
		
		mntmDeleteGun.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				String serial = (String) tblGuns.getModel().getValueAt(tblGuns.getSelectedRow(), 0);
				
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete "+ serial
						+"? This operation cannot be reversed!", "Confirm Delete", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION)
				{
					QueryGun newQuery = new QueryGun(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
					newQuery.deleteGun(serial);
					frmListGuns.setVisible(false);
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
				if (tblGuns.getSelectedRow() != -1)
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
